package com.saludmetropolitana.gestionpacientes.domain;

import static com.saludmetropolitana.gestionpacientes.domain.AgendaMedicaTestSamples.*;
import static com.saludmetropolitana.gestionpacientes.domain.CentroSaludTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.saludmetropolitana.gestionpacientes.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CentroSaludTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CentroSalud.class);
        CentroSalud centroSalud1 = getCentroSaludSample1();
        CentroSalud centroSalud2 = new CentroSalud();
        assertThat(centroSalud1).isNotEqualTo(centroSalud2);

        centroSalud2.setId(centroSalud1.getId());
        assertThat(centroSalud1).isEqualTo(centroSalud2);

        centroSalud2 = getCentroSaludSample2();
        assertThat(centroSalud1).isNotEqualTo(centroSalud2);
    }

    @Test
    void horaCentroSaludTest() {
        CentroSalud centroSalud = getCentroSaludRandomSampleGenerator();
        AgendaMedica agendaMedicaBack = getAgendaMedicaRandomSampleGenerator();

        centroSalud.addHoraCentroSalud(agendaMedicaBack);
        assertThat(centroSalud.getHoraCentroSaluds()).containsOnly(agendaMedicaBack);
        assertThat(agendaMedicaBack.getHorasCentroSalud()).isEqualTo(centroSalud);

        centroSalud.removeHoraCentroSalud(agendaMedicaBack);
        assertThat(centroSalud.getHoraCentroSaluds()).doesNotContain(agendaMedicaBack);
        assertThat(agendaMedicaBack.getHorasCentroSalud()).isNull();

        centroSalud.horaCentroSaluds(new HashSet<>(Set.of(agendaMedicaBack)));
        assertThat(centroSalud.getHoraCentroSaluds()).containsOnly(agendaMedicaBack);
        assertThat(agendaMedicaBack.getHorasCentroSalud()).isEqualTo(centroSalud);

        centroSalud.setHoraCentroSaluds(new HashSet<>());
        assertThat(centroSalud.getHoraCentroSaluds()).doesNotContain(agendaMedicaBack);
        assertThat(agendaMedicaBack.getHorasCentroSalud()).isNull();
    }
}
