package com.course.jpaconherencia.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BiciDTO {
    private String modelo;
    private Integer rodado;
}
