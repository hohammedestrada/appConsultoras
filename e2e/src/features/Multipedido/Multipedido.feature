@RMultipedido
Feature: Reservar un pedido con la opción Multipedido
  Como una Consultora debo tener la opcion de realizar una facturacion adelantada
  seleccionando el check Deseo Multifacturar hoy

Scenario Outline: Se realiza un pedido para consultoras que son Multipedido
  Given Ingreso al pase de pedido con el "<Usuario>" y "<Password>" del pais "<Pais>"
  When  Se ingresa el "<Producto>" y la "<Cantidad>" y agrego el producto
  And "<FlagM>" activo el botón para facturar hoy
  And "<Freserva>" se reserva el pedido
  Then Se termina con la reserva del pedido

Examples:
  |Usuario   |Password|Pais         |Producto|Cantidad|FlagM|Freserva|
  |0464944   |1       |Bolivia      |00010   |10      |Si   |Si      |
  |0029363875|1       |Colombia     |00010   |10      |Si   |Si      |
  |1711274108|1       |Ecuador      |00010   |10      |Si   |Si      |
  |0134143   |1       |ElSalvador   |00010   |10      |Si   |Si      |
  |0074379   |1       |Guatemala    |00010   |10      |Si   |Si      |
  |045351556 |1       |Peru         |00010   |10      |Si   |Si      |
  |0142392   |1       |RepDominicana|00010   |10      |Si   |Si      |
