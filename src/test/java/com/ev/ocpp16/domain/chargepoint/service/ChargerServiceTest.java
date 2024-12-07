package com.ev.ocpp16.domain.chargepoint.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.dto.api.ChgrQueryDTO;
import com.ev.ocpp16.domain.chargepoint.dto.api.ChgrsQueryDTO;
import com.ev.ocpp16.domain.chargepoint.entity.Charger;
import com.ev.ocpp16.domain.chargepoint.entity.Site;
import com.ev.ocpp16.domain.chargepoint.entity.enums.ChgrConnSt;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerRepository;
import com.ev.ocpp16.domain.chargepoint.repository.SiteRepository;
import com.ev.ocpp16.domain.common.exception.api.ApiException;
import com.ev.ocpp16.domain.common.exception.api.ApiExceptionStatus;
import com.ev.ocpp16.domain.member.entity.enums.Address;

@SpringBootTest
@Transactional
public class ChargerServiceTest {

    @Autowired
    private ChargerService chargerService;

    @Autowired
    private ChargerRepository chargerRepository;

    @Autowired
    private SiteRepository siteRepository;

    private Site testSite;
    private Charger charger1;
    private Charger charger2;

    @BeforeEach
    void setUp() throws Exception {
        // 테스트 주소 생성
        Address testAddress = new Address();
        setField(testAddress, "zipCode", "07666");
        setField(testAddress, "address1", "테스트 주소1");
        setField(testAddress, "address2", "테스트 주소2");

        // 테스트 사이트 생성
        testSite = new Site();
        setField(testSite, "name", "testSite");
        setField(testSite, "address", testAddress);
        siteRepository.save(testSite);

        // 테스트 충전기들 생성
        charger1 = new Charger();
        setField(charger1, "name", "Charger-1");
        setField(charger1, "serialNumber", "SN001");
        setField(charger1, "model", "TEST-MODEL-1");
        setField(charger1, "vendor", "TEST-VENDOR");
        setField(charger1, "firmwareVersion", "1.0.0");
        setField(charger1, "chgrConnSt", ChgrConnSt.CONNECTED);
        setField(charger1, "isActive", true);
        setField(charger1, "site", testSite);

        charger2 = new Charger();
        setField(charger2, "name", "Charger-2");
        setField(charger2, "serialNumber", "SN002");
        setField(charger2, "model", "TEST-MODEL-2");
        setField(charger2, "vendor", "TEST-VENDOR");
        setField(charger2, "firmwareVersion", "1.0.0");
        setField(charger2, "chgrConnSt", ChgrConnSt.DISCONNECTED);
        setField(charger2, "isActive", true);
        setField(charger2, "site", testSite);

        chargerRepository.save(charger1);
        chargerRepository.save(charger2);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @DisplayName("사이트의 모든 충전기 조회 성공")
    void getChgrsBySite_Success() {
        // given
        ChgrsQueryDTO.Request request = new ChgrsQueryDTO.Request();
        request.setSiteName(testSite.getName());

        // when
        ChgrsQueryDTO.Response response = chargerService.getChgrsBySite(request);

        // then
        assertThat(response.getChargers()).hasSize(2);
        assertThat(response.getChargers())
                .extracting("serialNumber")
                .containsExactlyInAnyOrder("SN001", "SN002");
        assertThat(response.getChargers())
                .extracting("model")
                .containsExactlyInAnyOrder("TEST-MODEL-1", "TEST-MODEL-2");
        assertThat(response.getChargers())
                .extracting("chargerConnectorStatus")
                .containsExactlyInAnyOrder(ChgrConnSt.CONNECTED, ChgrConnSt.DISCONNECTED);
    }

    @Test
    @DisplayName("특정 충전기 조회 성공")
    void getChgrBySite_Success() {
        // given
        ChgrQueryDTO.Request request = new ChgrQueryDTO.Request();
        request.setSiteName(testSite.getName());

        // when
        ChgrQueryDTO.Response response = chargerService.getChgrBySite("SN001", request);

        // then
        assertThat(response.getSerialNumber()).isEqualTo("SN001");
        assertThat(response.getModel()).isEqualTo("TEST-MODEL-1");
        assertThat(response.getChargerConnectorStatus()).isEqualTo(ChgrConnSt.CONNECTED);
        assertThat(response.getSiteName()).isEqualTo(testSite.getName());
    }

    @Test
    @DisplayName("존재하지 않는 사이트로 조회시 예외 발생")
    void getChgrsBySite_WithInvalidSite_ThrowsException() {
        // given
        ChgrsQueryDTO.Request request = new ChgrsQueryDTO.Request();
        request.setSiteName("nonexistentSite");

        // when & then
        assertThatThrownBy(() -> chargerService.getChgrsBySite(request))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiExceptionStatus", ApiExceptionStatus.INVALID_SITE_NAME);
    }

    @Test
    @DisplayName("존재하지 않는 충전기 조회시 예외 발생")
    void getChgrBySite_WithInvalidSerialNumber_ThrowsException() {
        // given
        ChgrQueryDTO.Request request = new ChgrQueryDTO.Request();
        request.setSiteName(testSite.getName());

        // when & then
        assertThatThrownBy(() -> chargerService.getChgrBySite("INVALID-SN", request))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiExceptionStatus", ApiExceptionStatus.INVALID_SERIAL_NUMBER);
    }
}
