package ru.netology.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

public class MedicalServiceMockitoTest {
    @ParameterizedTest
    @MethodSource("checkBloodPressureMethodSource")
    public void parametrizedMockitoTestCheckBloodPressure(String id, int high, int low, int expectedTimes) {
        //arrange
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        Mockito.when(patientInfoRepository.getById("01"))
                .thenReturn(new PatientInfo("01", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        Mockito.when(patientInfoRepository.getById("02"))
                .thenReturn(new PatientInfo("02", "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78))));

        BloodPressure bloodPressure = new BloodPressure(high, low);

        //act
        medicalService.checkBloodPressure(id, bloodPressure);
        System.out.println();

        //assert
        Mockito.verify(alertService, Mockito.times(expectedTimes)).send(Mockito.anyString());
    }

    public static Stream<Arguments> checkBloodPressureMethodSource() {
        return Stream.of(
                Arguments.of("01", 120, 80, 0),
                Arguments.of("02", 110, 70, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("checkTemperatureMethodSource")
    public void parametrizedMockitoTestCheckTemperature(String id, BigDecimal temperature, int expectedTimes) {
        //arrange
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        Mockito.when(patientInfoRepository.getById("01"))
                .thenReturn(new PatientInfo("01", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        Mockito.when(patientInfoRepository.getById("02"))
                .thenReturn(new PatientInfo("02", "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78))));

        //act
        medicalService.checkTemperature(id, temperature);
        System.out.println();

        //assert
        Mockito.verify(alertService, Mockito.times(expectedTimes)).send(Mockito.anyString());
    }

    public static Stream<Arguments> checkTemperatureMethodSource() {
        return Stream.of(
                Arguments.of("01", new BigDecimal("34.0"), 1),
                Arguments.of("02", new BigDecimal("36.6"), 0)
        );
    }

    @Test
    public void mockitoTestCheckBloodPressureMessage() {
        //arrange
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        Mockito.when(patientInfoRepository.getById("01"))
                .thenReturn(new PatientInfo("01", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        BloodPressure bloodPressure = new BloodPressure(140, 100);

        String expectedMessage = "Warning, patient with id: 01, need help";

        //act
        medicalService.checkBloodPressure("01", bloodPressure);
        System.out.println();

        //assert
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals(expectedMessage, argumentCaptor.getValue());
    }

    @Test
    public void mockitoTestCheckTemperatureMessage() {
        //arrange
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        Mockito.when(patientInfoRepository.getById("01"))
                .thenReturn(new PatientInfo("01", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        BigDecimal temperature = new BigDecimal("34.0");

        String expectedMessage = "Warning, patient with id: 01, need help";

        //act
        medicalService.checkTemperature("01", temperature);
        System.out.println();

        //assert
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals(expectedMessage, argumentCaptor.getValue());
    }
}