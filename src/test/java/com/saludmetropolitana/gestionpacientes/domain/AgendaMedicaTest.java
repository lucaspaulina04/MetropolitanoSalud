package com.saludmetropolitana.gestionpacientes.domain;

import static com.saludmetropolitana.gestionpacientes.domain.AgendaMedicaTestSamples.*;
import static com.saludmetropolitana.gestionpacientes.domain.CentroSaludTestSamples.*;
import static com.saludmetropolitana.gestionpacientes.domain.MedicosTestSamples.*;
import static com.saludmetropolitana.gestionpacientes.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.saludmetropolitana.gestionpacientes.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgendaMedicaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgendaMedica.class);
        AgendaMedica agendaMedica1 = getAgendaMedicaSample1();
        AgendaMedica agendaMedica2 = new AgendaMedica();
        assertThat(agendaMedica1).isNotEqualTo(agendaMedica2);

        agendaMedica2.setId(agendaMedica1.getId());
        assertThat(agendaMedica1).isEqualTo(agendaMedica2);

        agendaMedica2 = getAgendaMedicaSample2();
        assertThat(agendaMedica1).isNotEqualTo(agendaMedica2);
    }

    @Test
    void pacienteHoraTest() {
        AgendaMedica agendaMedica = getAgendaMedicaRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        agendaMedica.setPacienteHora(pacienteBack);
        assertThat(agendaMedica.getPacienteHora()).isEqualTo(pacienteBack);

        agendaMedica.pacienteHora(null);
        assertThat(agendaMedica.getPacienteHora()).isNull();
    }

    @Test
    void horasMedicasTest() {
        AgendaMedica agendaMedica = getAgendaMedicaRandomSampleGenerator();
        Medicos medicosBack = getMedicosRandomSampleGenerator();

        agendaMedica.setHorasMedicas(medicosBack);
        assertThat(agendaMedica.getHorasMedicas()).isEqualTo(medicosBack);

        agendaMedica.horasMedicas(null);
        assertThat(agendaMedica.getHorasMedicas()).isNull();
    }

    @Test
    void horasCentroSaludTest() {
        AgendaMedica agendaMedica = getAgendaMedicaRandomSampleGenerator();
        CentroSalud centroSaludBack = getCentroSaludRandomSampleGenerator();

        agendaMedica.setHorasCentroSalud(centroSaludBack);
        assertThat(agendaMedica.getHorasCentroSalud()).isEqualTo(centroSaludBack);

        agendaMedica.horasCentroSalud(null);
        assertThat(agendaMedica.getHorasCentroSalud()).isNull();
    }
}
