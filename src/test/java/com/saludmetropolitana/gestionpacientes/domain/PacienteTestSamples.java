package com.saludmetropolitana.gestionpacientes.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Paciente getPacienteSample1() {
        return new Paciente()
            .id(1L)
            .nombre("nombre1")
            .apellido("apellido1")
            .fechanacimiento("fechanacimiento1")
            .edad("edad1")
            .direccion("direccion1")
            .email("email1")
            .numero("numero1");
    }

    public static Paciente getPacienteSample2() {
        return new Paciente()
            .id(2L)
            .nombre("nombre2")
            .apellido("apellido2")
            .fechanacimiento("fechanacimiento2")
            .edad("edad2")
            .direccion("direccion2")
            .email("email2")
            .numero("numero2");
    }

    public static Paciente getPacienteRandomSampleGenerator() {
        return new Paciente()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .fechanacimiento(UUID.randomUUID().toString())
            .edad(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .numero(UUID.randomUUID().toString());
    }
}
