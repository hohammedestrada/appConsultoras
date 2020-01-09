Feature: Ingresar al Pase de Pedido, Reservar o Guardar un pedido
  Como una Consultora debo poder
  ingresar al pase de pedido
  agregar los productos que desee
  y finalmente guardar o reservar mis pedidos
  0 es para el mensaje de Pedido Guardado
  1 es para el mensaje de Pedido Reservado
  2 es para el mensaje de Pedido Reservado en la Oferta finalmente
  3 es cuando la reserva no se realiza por la validación interactiva.

@Esika @Reservar
Scenario Outline: Se Reserva el Pedido en ESIKA
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>" ,ingreso al pase de pedido
  When Se ingresa el "<Producto>" y la "<Cantidad>" y agrego el producto
  And Si la consultora "<Flag>" tiene Regalo
  And Se envia a Reservar o Guardar el pedido
  Then Se "<Reserva>" el Pedido

Examples:
  |Usuario        |Password|Pais         |Producto|Cantidad|Flag|Reserva|
  |usuariopruebape|1234567 |Peru         |00010   |10      |No  |2      |

@LBEL @Reservar
Scenario Outline: Se Reserva el Pedido en LBEL
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>" ,ingreso al pase de pedido
  When Se ingresa el "<Producto>" y la "<Cantidad>" y agrego el producto
  And  Si la consultora "<Flag>" tiene Regalo
  And  Se envia a Reservar o Guardar el pedido
  Then Se "<Reserva>" el Pedido

Examples:
  |Usuario        |Password|Pais        |Producto|Cantidad|Flag|Reserva|
  |usuariopruebacr|1234567 |CostaRica   |00010   |7       |No  |2      |
  |usuariopruebamx|1234567 |Mexico      |00010   |7       |No  |2      |
  |usuariopruebapa|1       |Panama      |00010   |7       |No  |2      |
  |usuariopruebapr|1234567 |PuertoRico  |00010   |7       |No  |2      |

@PasePedido @ReservaconRegalo
Scenario Outline: Se Reserva el Pedido, consultora con regalo en ESIKA
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>" ,ingreso al pase de pedido
  When Se ingresa el "<Producto>" y la "<Cantidad>" y agrego el producto
  And Si la consultora "<Flag>" tiene Regalo
  And Se envia a Reservar o Guardar el pedido
  Then Se "<Reserva>" el Pedido

Examples:
  |Usuario        |Password|Pais         |Producto|Cantidad|Flag|Reserva|
  |usuariopruebaco|1234567 |Colombia     |00010   |10      |No  |5      |
  |usuariopruebaec|1234567 |Ecuador      |00010   |10      |No  |5      |
  |usuariopruebasv|1234567 |ElSalvador   |00010   |10      |No  |5      |
  |usuariopruebagt|1234567 |Guatemala    |00010   |10      |No  |5      |
  |usuariopruebape|1234567 |Peru         |00010   |10      |No  |5      |
  |usuariopruebado|1234567 |RepDominicana|00010   |10      |No  |5      |

@GuardarPedido
Scenario Outline:  Se Guarda el pedido
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>" ,ingreso al pase de pedido
  When Se ingresa el "<Producto>" y la "<Cantidad>" y agrego el producto
  And Si la consultora "<Flag>" tiene Regalo
  And Se envia a Reservar o Guardar el pedido
  Then Se "<Reserva>" el Pedido

Examples:
  |Usuario  |Password|Pais      |Producto|Cantidad|Flag|Reserva|
  |0537250  |1       |Bolivia   |03289   |4       |No  |0      |
  |027304656|1       |Peru      |03289   |4       |No  |0      |

@ReservaFallida
Scenario Outline:  No se reserva el Pedido
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>" ,ingreso al pase de pedido
  When Se ingresa el "<Producto>" y la "<Cantidad>" y agrego el producto
  And Si la consultora "<Flag>" tiene Regalo
  And Se envia a Reservar o Guardar el pedido
  Then Se "<Reserva>" el Pedido

Examples:
  |Usuario   |Password|Pais    |Producto|Cantidad|Flag|Reserva|
  |0032091418|1	      |Colombia|Null    |0       |No  |3  	  |
  |0043649499|1	      |Colombia|Null    |0       |No  |3  	  |
