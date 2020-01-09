Feature: Modificar el pedido guardado o reservado
  Como una Consultora debo poder modificar la reserva o un pedido guardado

@ModificarPasePedido @AgregarProducto
Scenario Outline: Se modifica la reserva del pedido agregando un producto
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>" ,ingreso al pase de pedido
  When El Boton dice "<Boton>" entonces
  And  depende de la operacion "<Operacion>", se toma el "<Producto>" y la "<Cantidad>"
  And  Terminada la operacion, "se guarda o reserva" el pedido
  Then  Segun la operacion se termina el pedido "<Reserva>"

   #1 : Agregar Producto
   #2 : Modificar Cantidad del Producto
   #3 : Eliminar Producto

Examples:
  |Usuario        |Password|Pais        |Boton     |Operacion|Producto|Cantidad|Reserva|
  |usuariopruebabo|1234567|Bolivia      |GUARDAR   |2        |00010   |2       |2      |
  |usuariopruebaco|1234567|Colombia     |GUARDAR   |2        |00010   |2       |2      |
  |usuariopruebaec|1234567|Ecuador      |GUARDAR   |2        |00010   |2       |2      |
  |usuariopruebasv|1234567|ElSalvador   |GUARDAR   |2        |00010   |2       |2      |
  |usuariopruebagt|1234567|Guatemala    |GUARDAR   |2        |00010   |2       |2      |
  |usuariopruebape|1234567|Peru         |GUARDAR   |2        |00010   |2       |2      |
  |usuariopruebado|1234567|RepDominicana|GUARDAR   |2        |00010   |2       |2      |

@Esika @ModificarProducto
Scenario Outline: Se modifica el pedido cambiando la cantidad del producto en ESIKA
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>" ,ingreso al pase de pedido
  When El Boton dice "<Boton>" entonces
  And depende de la operacion "<Operacion>", se toma el "<Producto>" y la "<Cantidad>"
  And Terminada la operacion, "se guarda o reserva" el pedido
  Then Segun la operacion se termina el pedido "<Reserva>"

   #1 : Agregar Producto
   #2 : Modificar Cantidad del Producto
   #3 : Eliminar Producto

Examples:
  |Usuario        |Password|Pais        |Boton     |Operacion|Producto|Cantidad|Reserva|
  |usuariopruebape|1234567|Peru         |GUARDAR   |2        |00010   |2       |2      |

@LBEL @ModificarProducto
Scenario Outline: Se modifica el pedido cambiando la cantidad del producto en LBEL
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>" ,ingreso al pase de pedido
  When El Boton dice "<Boton>" entonces
  And  depende de la operacion "<Operacion>", se toma el "<Producto>" y la "<Cantidad>"
  And  Terminada la operacion, "se guarda o reserva" el pedido
  Then Segun la operacion se termina el pedido "<Reserva>"

   #1 : Agregar Producto
   #2 : Modificar Cantidad del Producto
   #3 : Eliminar Producto

Examples:
  |Usuario        |Password|Pais        |Boton     |Operacion|Producto|Cantidad|Reserva|
  |usuariopruebacr|1234567|CostaRica    |GUARDAR   |2        |00010   |2       |2      |
  |usuariopruebamx|1234567|Mexico       |GUARDAR   |2        |00010   |2       |2      |
  |usuariopruebapa|1      |Panama       |GUARDAR   |2        |00010   |2       |2      |
  |usuariopruebapr|1234567|PuertoRico   |GUARDAR   |2        |00010   |2       |2      |

@Esika @EliminarProducto
Scenario Outline: Se modifica el pedido eliminando del producto en ESIKA
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>" ,ingreso al pase de pedido
  When El Boton dice "<Boton>" entonces
  And  depende de la operacion "<Operacion>", se toma el "<Producto>" y la "<Cantidad>"
  And  Terminada la operacion, "se guarda o reserva" el pedido
  Then Segun la operacion se termina el pedido "<Reserva>"

   #1 : Agregar Producto
   #2 : Modificar Cantidad del Producto
   #3 : Eliminar Producto

Examples:
  |Usuario        |Password|Pais         |Boton     |Operacion|Producto|Cantidad|Reserva|
  |usuariopruebape|1234567 |Peru         |GUARDAR   |3        |00010   |0       |5      |

@LBEL @EliminarProducto
Scenario Outline: Se modifica el pedido eliminando del producto en LBEL
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>" ,ingreso al pase de pedido
  When El Boton dice "<Boton>" entonces
  And  depende de la operacion "<Operacion>", se toma el "<Producto>" y la "<Cantidad>"
  And  Terminada la operacion, "se guarda o reserva" el pedido
  Then Segun la operacion se termina el pedido "<Reserva>"

   #1 : Agregar Producto
   #2 : Modificar Cantidad del Producto
   #3 : Eliminar Producto

Examples:
  |Usuario        |Password|Pais         |Boton     |Operacion|Producto|Cantidad|Reserva|
  |usuariopruebacr|1234567 |CostaRica    |GUARDAR   |3        |00010   |0       |2      |
  |usuariopruebamx|1234567 |Mexico       |GUARDAR   |3        |00010   |0       |2      |
  |usuariopruebapa|1       |Panama       |GUARDAR   |3        |00010   |0       |2      |
  |usuariopruebapr|1234567 |PuertoRico   |GUARDAR   |3        |00010   |0       |2      |
