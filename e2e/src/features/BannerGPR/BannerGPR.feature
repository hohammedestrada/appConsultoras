Feature: Ver el Banner GPR
  Como una Consultora
  Yo quiero ingresar al app Esik / Lbel Conmigo
  y visualizar el estado de facturación de mi pedido.

@BannerGPR
Scenario Outline:  Ver Banner GPR
  Given Con el "<Pais>" ingreso "<Usuario>", "<Password>", click en el boton "INICIAR SESION"
  When Ingreso al APP de Consultoras
  Then "<GPR>" se muestra el Banner de Pedido "<Pedido>"

Examples:
  |Pais    |Usuario   |Password|GPR|Pedido   |
  |Colombia|0035428844|1		   |Si |Rechazado|
  |Colombia|1038137947|1		   |Si |Rechazado|
  |Colombia|0050919642|1		   |Si |Rechazado|
  |Colombia|0043624242|1		   |Si |Rechazado|
  |Colombia|0021818707|1		   |Si |Rechazado|

@DetalleGPRMensaje
Scenario Outline:  Validar detalle de Banner GPR
  Given Con el "<Pais>" ingreso "<Usuario>", "<Password>", click en el boton "INICIAR SESION"
  When Ingreso al APP de Consultoras
  Then "<GPR>" se muestra el Banner de Pedido "<Pedido>"
  And con las validaciones "<Validaciones>"
        # 1 : Monto Máximo
        # 2 : Monto Mínimo
        # 3 : Deuda
        # 4 : Desviacion (Monto Permitido)

Examples:
  |Pais    |Usuario   |Password|GPR|Pedido   |Validaciones|
  |Colombia|1038547339|1		   |Si |Rechazado|31          |
