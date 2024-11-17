package com.saludmetropolitana.gestionpacientes.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgendaMedicaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AgendaMedica getAgendaMedicaSample1() {
        return new AgendaMedica()
            .id(1L)
            .fechaHora("fechaHora1")
            .pacienteID("pacienteID1")
            .medicoID("medicoID1")
            .centroSaludID("centroSaludID1")
            .estadoHora("estadoHora1");
    }

    public static AgendaMedica getAgendaMedicaSample2() {
        return new AgendaMedica()
            .id(2L)
            .fechaHora("fechaHora2")
            .pacienteID("pacienteID2")
            .medicoID("medicoID2")
            .centroSaludID("centroSaludID2")
            .estadoHora("estadoHora2");
    }

    public static AgendaMedica getAgendaMedicaRandomSampleGenerator() {
        return new AgendaMedica()
            .id(longCount.incrementAndGet())
            .fechaHora(UUID.randomUUID().toString())
            .pacienteID(UUID.randomUUID().toString())
            .medicoID(UUID.randomUUID().toString())
            .centroSaludID(UUID.randomUUID().toString())
            .estadoHora(UUID.randomUUID().toString());
    }
}
