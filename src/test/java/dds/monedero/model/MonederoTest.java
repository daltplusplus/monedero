package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Poner() {
    cuenta.poner(1500);
    assertEquals(cuenta.getMovimientos().stream().count(), 1);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(cuenta.getMovimientos().stream().count(), 3);
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  public void MontoExtraido() {
  	cuenta.setSaldo(2000);
  	cuenta.sacar(500);
    cuenta.sacar(400);
    assertEquals(cuenta.getMontoExtraidoA(LocalDate.now()), 900);
  }
  
  @Test
  public void FueExtraido() {
  	cuenta.setSaldo(2000);
  	cuenta.poner(200);
  	cuenta.sacar(200);
  	assertEquals(cuenta.getMovimientos().stream().filter(movimiento -> movimiento.fueExtraido(LocalDate.now())).count(),1);
  }
  
  @Test
  public void FueDepositado() {
  	cuenta.setSaldo(2000);
  	cuenta.poner(200);
  	cuenta.sacar(200);
  	assertEquals(cuenta.getMovimientos().stream().filter(movimiento -> movimiento.fueDepositado(LocalDate.now())).count(),1);
  }
}