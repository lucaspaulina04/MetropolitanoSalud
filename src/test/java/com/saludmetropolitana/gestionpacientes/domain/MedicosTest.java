package com.saludmetropolitana.gestionpacientes.domain;

import static com.saludmetropolitana.gestionpacientes.domain.AgendaMedicaTestSamples.*;
import static com.saludmetropolitana.gestionpacientes.domain.MedicosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.saludmetropolitana.gestionpacientes.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MedicosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicos.class);
        Medicos medicos1 = getMedicosSample1();
        Medicos medicos2 = new Medicos();
        assertThat(medicos1).isNotEqualTo(medicos2);

        medicos2.setId(medicos1.getId());
        assertThat(medicos1).isEqualTo(medicos2);

        medicos2 = getMedicosSample2();
        assertThat(medicos1).isNotEqualTo(medicos2);
    }

    @Test
    void horaMedicoTest() {
        Medicos medicos = getMedicosRandomSampleGenerator();
        AgendaMedica agendaMedicaBack = getAgendaMedicaRandomSampleGenerator();

        medicos.addHoraMedico(agendaMedicaBack);
        assertThat(medicos.getHoraMedicos()).containsOnly(agendaMedicaBack);
        assertThat(agendaMedicaBack.getHorasMedicas()).isEqualTo(medicos);

        medicos.removeHoraMedico(agendaMedicaBack);
        assertThat(medicos.getHoraMedicos()).doesNotContain(agendaMedicaBack);
        assertThat(agendaMedicaBack.getHorasMedicas()).isNull();

        medicos.horaMedicos(new HashSet<>(Set.of(agendaMedicaBack)));
        assertThat(medicos.getHoraMedicos()).containsOnly(agendaMedicaBack);
        assertThat(agendaMedicaBack.getHorasMedicas()).isEqualTo(medicos);

        medicos.setHoraMedicos(new HashSet<>());
        assertThat(medicos.getHoraMedicos()).doesNotContain(agendaMedicaBack);
        assertThat(agendaMedicaBack.getHorasMedicas()).isNull();
    }
}
