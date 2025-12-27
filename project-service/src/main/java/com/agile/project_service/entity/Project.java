package com.agile.project_service.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "projects")
public class Project extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "user_id")
    private Set<Long> memberIds = new HashSet<>();
}
