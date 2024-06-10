# Como evitar N + 1 Queries con JPA

Este ejemplo nos permite enteder y aprender a evitar el problema de los (N + 1) queries en JPA. 
Consiste en un servicio REST realizado con String Boot 3.2, que contiene endpoints para consultar los productos y fabricantes de esos productos.

## Prerequisitos
Para simular un ambiente productivo nos conectaremos a un base de datos PostgresSQL. 
Solo hace falta tener instalado Docker Desktop (el pack docker engine y docker compose). La opción más directa es instalar [DockerEngine](https://docs.docker.com/engine/install/) y elegir la que corresponde a tu sistema operativo. 

En el directorio raiz de este proyecto encontraras un archivo llamado **docker-compose.yml** que hace toda la magia. Por un lado levanta un contenedor con el motor PostgresSQL, y además, levanta un segundo conenedor con el cliente web de pgAdmin que pueden conectarse al PostgresSQL. Solo tenes que correr el siguiente comando:
```
docker compose up
```
### pgAdmin
Para controlar que todo esta OK, voy a acceder al motor PostgresSQL a través del cliente pgAdmin.
Según la configuración que figura dentro del archivo **docker-compose.yml** el contenedor pgAdmin se disponibiliza en el puerto 8090 de la máquina local http://localhost:8090, tenés que ingresar **fluxit@flux.com** como usuario y contraseña **admin** para acceder a este cliente web.

Una vez que ingresás tenés que registrar el server, le podés poner el nombre que quieras, en el Host Name sí hay que utilizar el mismo nombre que define el archivo **docker-compose.yml** como container de Postgres, en este caso esta configurado **productos-fabricantes_sql**. El puerto es 5432, el username y la contraseña deben ser **fluxdb:fluxdbpwd** 

### Configuración del source
La conexión a la base se configura en el archivo application.properties
```
spring.datasource.url=jdbc:postgresql://0.0.0.0:5432/entitystore
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=fluxdb
spring.datasource.password=fluxdbpwd
```

- 0.0.0.0 apunta a nuestro contenedor de Docker y 5432 es el puerto que expone el contenedor para conectarnos a PostgresSQL
- **entitystore**  es el nombre de base de datos creada en el script de creación de base de datos
- el usuario y contraseña son los definidos en el mismo script de creación de base de datos

## Dominio
El requerimiento que debemos modelar es el siguiente:
- Un producto pueden ser realizados muchos fabricantes
- Un fabricante puede producir muchos productos
- Las altas/bajas de producto y fabricante no estan relacionadas.

Desde el punto de vista de objetos, producto y fabricante, tienen una relación **many_to_many**. Y por definicion no hay relación en cascada porque tanto el producto como el fabricante tiene ciclos de vida independientes.

## Set de Datos
Utilizaremos un configuración y una clase de boostraping para generar un set de datos.

Si la configuración ````data.initialization.enabled=boolean```` esta en **true** entonces la clase  **ProductoBootstrap** generará el siguiente set de datos.
- Genera 5000 fabricantes que se persisten en la base de datos.
- Genera 15000 productos que se persisen en la base de datos.
- Por cada producto se le asocian aleatoriamente entre 0 y 10 fabricantes de la lista de 5000 fabricantes

## Endpoints
Los endpoint que publica el web server son:

|Método|Endpoint|Pagina| Comentarios                                                                      |
|------|--------|------|----------------------------------------------------------------------------------|
|Get|/productos|true| Retorna todos los productos con los fabricanes correspondiente por cada prodcuto |
|Get|/fabricantes|false| Retorna todos los fabricantes sin productos asociados                            |
|Get|/fabricantes/:id|false| Retorna un fabricante por id con los productos asociados                         |

Dado que tenemos una gran cantidad de productos, decidimos paginar los resultados que envía el repositorio, por defecto se traen 50 productos. 
Cuando consumimos el endpoint de producto podremos indicar por query parameter los valores page, size y sort

## EAGER
Qué pasa si definimos dentro del producto la configuracion EAGER contra fabricantes?
```
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    .....

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Fabricante> fabricantes;

}
```
Con esta configuración le estamos indicando que cada vez que tomemos los datos de un producto también traeremos los fabricantes. Si en el controller permitimos paginar muchos productos, vamos comenzar a ver problemas de perfromace debido a los N + 1 Queries.

SI hacemos una llamada a productos http://localhost:8080/productos.  Podemos ver que:

- un primer query que trae todos los productos
```
Hibernate: 
    select
        p1_0.id,
        p1_0.codigo,
        p1_0.disponible,
        p1_0.fecha_lanzamiento,
        p1_0.nombre 
    from
        producto p1_0 
    offset
        ? rows 
    fetch
        first ? rows only
```
- y por cada producto, existe otro query que baja la información de sus fabricantes (el temido n, la lista de productos que paginados pueden ser 5... o 1000)
```
Hibernate: 
    select
        f1_0.producto_id,
        f1_1.id,
        f1_1.categoria,
        f1_1.nombre 
    from
        producto_fabricantes f1_0 
    join
        fabricante f1_1 
            on f1_1.id=f1_0.fabricantes_id 
    where
        f1_0.producto_id=?
  ```

## Lazy ...y listo?
Podemos pensar entonces que si configuramos la relación producto-fabricantes como **lazy**, habremos resuelto nuestro problema:

```
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    .....

    @ManyToMany(fetch = FetchType.LAZY)
    Set<Fabricante> fabricantes;

}
```
Reiniciamos la aplicación, llamamos nuevamente a nuestro endpoint de prodcutos y ahora todo es **llanto, dolor y desazón** porque ahora tenenos esta hermosa exeption:  **org.hibernate.LazyInitializationException**

El mensaje de error es bastante claro no:

```
org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.fluxit.talks.jpa.domain.Producto.fabricantes: could not initialize proxy - no Session
```

El problema está en que los fabricantes son una colección lazy en producto, y que tenemos la configuración

```
spring.jpa.open-in-view=false
```
Esta configuracion optimiza el uso de sesiones de la base de datos y hace que la sesión se cierre cuando:

- en el service cuyo método se demarca con la anotación @Transactional (sea read-only o no)
- o bien en este caso dentro del método del ProductoRepository
es decir, que en el momento en que el controller quiere serializar a JSON la respuesta del endpoint, ya no tenemos una sesión abierta y se lanza la excepción.

A listo... cambiamos la configuración a:
```
spring.jpa.open-in-view=true
```
que es la opción por defecto de Springboot pero no mejora nuestro problema, volvemos a tener el mismo comportamiento que en el primer caso (con la configuración EAGER), porque en la serialización a JSON estamos yendo a buscar los proveedores por cada uno de los productos.

## Entity Graph
Configurar de modo Lazy es parte de la solución, pero una buena configuración para optimización del pool de conexiones es usar la configuración ```spring.jpa.open-in-view=false```.

Entonces debemos ajustar nuestro query para que los productos los resuelva haciendo LEFT JOIN hacia la tabla de fabricantes en la misma consulta. Así evitamos el problema de los (n + 1) queries:

```
@Repository
public interface ProductoRepository extends PagingAndSortingRepository<Producto, Long>, CrudRepository<Producto, Long> {
    @EntityGraph(attributePaths = {"fabricantes"})
    @Query("Select p from Producto p")
    Page<Producto> findAllWithFabricantes(Pageable pageable);
}
```
Ahora sí
- LAZY
- open-in-view=false
- @EntityGraph

```
Hibernate: 
    select
        p1_0.id,
        p1_0.codigo,
        p1_0.disponible,
        f1_0.producto_id,
        f1_1.id,
        f1_1.categoria,
        f1_1.nombre,
        p1_0.fecha_lanzamiento,
        p1_0.nombre 
    from
        producto p1_0 
    left join
        producto_fabricantes f1_0 
            on p1_0.id=f1_0.producto_id 
    left join
        fabricante f1_1 
            on f1_1.id=f1_0.fabricantes_id
```

Aún cuando hay que traer 1000 productos, la deserialización de la base hacia el modelo de objetos en la JDK y el posterior render a JSON tarda bastante menos, **pero sobre todo, no perjudicamos a la base haciendo 1001 consultas**.

## Collection de Postman
Dentro del proyecto queda una [collection de postman](./N+1Queries.postman_collection.json) para poder probar. 
