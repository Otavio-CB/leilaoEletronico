package br.gov.sp.fatec.lp2.config;

import io.micronaut.context.annotation.Bean;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

@Singleton
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}