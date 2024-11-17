package com.saludmetropolitana.gestionpacientes.domain;

import static com.saludmetropolitana.gestionpacientes.domain.AgendaMedicaTestSamples.*;
import static com.saludmetropolitana.gestionpacientes.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.saludmetropolitana.gestionpacientes.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PacienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paciente.class);
        Paciente paciente1 = getPacienteSample1();
        Paciente paciente2 = new Paciente();
        assertThat(paciente1).isNotEqualTo(paciente2);

        paciente2.setId(paciente1.getId());
        assertThat(paciente1).isEqualTo(paciente2);

        paciente2 = getPacienteSample2();
        assertThat(paciente1).isNotEqualTo(paciente2);
    }

    @Test
    void pacientesTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        AgendaMedica agendaMedicaBack = getAgendaMedicaRandomSampleGenerator();

        paciente.addPacientes(agendaMedicaBack);
        assertThat(paciente.getPacientes()).containsOnly(agendaMedicaBack);
        assertThat(agendaMedicaBack.getPacienteHora()).isEqualTo(paciente);

        paciente.removePacientes(agendaMedicaBack);
        assertThat(paciente.getPacientes()).doesNotContain(agendaMedicaBack);
        assertThat(agendaMedicaBack.getPacienteHora()).isNull();

        paciente.pacientes(new HashSet<>(Set.of(agendaMedicaBack)));
        assertThat(paciente.getPacientes()).containsOnly(agendaMedicaBack);
        assertThat(agendaMedicaBack.getPacienteHora()).isEqualTo(paciente);

        paciente.setPacientes(new HashSet<>());
        assertThat(paciente.getPacientes()).doesNotContain(agendaMedicaBack);
        assertThat(agendaMedicaBack.getPacienteHora()).isNull();
    }
}
