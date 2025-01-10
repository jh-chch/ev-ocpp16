# OCPP 1.6 Communication Server  
이 프로젝트는 OCPP(Open Charge Point Protocol) 1.6 표준을 준수하는 통신 서버입니다.
- **OCPP 1.6 표준 준수**
- **충전 테스트 시뮬레이터**:  
  - [MicroOcppSimulatorCustom](https://github.com/jh-chch/MicroOcppSimulator)  
    - 해당 프로젝트에 맞게 커스터마이즈된 시뮬레이터입니다.  
    - 충전 프로세스를 테스트하고 검증할 수 있습니다.  

<br/>

## MicroOcppSimulatorCustom 사용 방법  
1. **Connectors 메뉴**  
   - 사용할 커넥터를 선택합니다.  
2. **idTag 설정**  
   - `Transaction Options` 섹션의 `Tag ID` 필드에 원하는 회원의 `idTag`를 입력하거나,  
     `Insert Tag ID` 버튼을 클릭하여 자동 입력합니다.  
3. **트랜잭션 업데이트**  
   - `Update Transaction` 버튼을 클릭합니다.  
4. **충전 시작/종료 테스트**  
   - 필요한 경우 `Status` 메뉴의 `Plugged` 또는 `Unplugged` 버튼을 통해 충전 시작/종료를 테스트합니다.

<br/>

## Docker 환경에서 실행  
```bash
docker build -t ocpp-server .
```
```bash
docker run -d -p 8080:8080 --name ocpp-server ocpp-server
```

<br/>

## 충전 시퀀스 다이어그램
![Sequence Diagram](https://github.com/user-attachments/assets/966cc245-666e-4338-98e4-b6d722b1076d) 
