package com.projectx.resumes.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class StackItems {
    @Column(name = "stack_name")
    private String stackName;
    @Column(name = "experience")
    private String experience;
}
