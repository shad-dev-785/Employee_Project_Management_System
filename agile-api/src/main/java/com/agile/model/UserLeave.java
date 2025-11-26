package com.agile.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_leaves")
@NoArgsConstructor
public class UserLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private int cl=3;
    private int el=2;
    private int sl=3;
    private int pl=12;
    public UserLeave(Long userId){
        this.userId= userId;
        this.cl=cl;
        this.sl=sl;
        this.pl=pl;
        this.el=el;
    }
}
