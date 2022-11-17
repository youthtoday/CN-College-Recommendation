package com.li.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageHelper<T> {
    private int count;//总记录数
    private int pageCount;//总页数
    private List<T> list;
}
