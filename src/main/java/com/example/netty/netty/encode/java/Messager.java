package com.example.netty.netty.encode.java;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Messager implements Serializable {
    private String content;
    private Date datetime;
}
