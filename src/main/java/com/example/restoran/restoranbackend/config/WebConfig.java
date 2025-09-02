package com.example.restoran.restoranbackend.config;

import com.example.restoran.restoranbackend.filters.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebConfig {
  @Autowired
  private JwtFilter jwtFilter;

}