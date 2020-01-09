Feature: Ingresar al Pase de Pedido, Realizar las operaciones en una sola ejecución
  Como una Consultora debo poder
  ingresar al pase de pedido
  Agregar un producto
  Luego modificar el producto
  Eliminar el producto.

@FlujoCompletoEsika
Scenario Outline: Se agrega reserva, modifica y elimina un poducto para ESIKA
  Given Ingreso para realizar un pedido con el "<Usuario>" y "<Password>" del pais "<Pais>"
  When Se ingresa el "<Producto>" y la "<Cantidad>" y agrego el producto
  And  Se modifica el producto con la cantidad "<adicional>"
  And  Se elimina el producto del pase de pedido
  Then Se termina el flujo del Pase de Pedido

Examples:
  |Usuario        |Password|Pais         |Producto|Cantidad|adicional|
  |usuariopruebape|1234567 |Peru         |00006   |6       |2        |

@FlujoCompletoLBEL
Scenario Outline: Se agrega reserva, modifica y elimina un poducto para LBEL
  Given Ingreso para realizar un pedido con el "<Usuario>" y "<Password>" del pais "<Pais>"
  When Se ingresa el "<Producto>" y la "<Cantidad>" y agrego el producto
  And Se modifica el producto con la cantidad "<adicional>"
  And Se elimina el producto del pase de pedido
  Then Se termina el flujo del Pase de Pedido

Examples:
  |Usuario        |Password|Pais         |Producto|Cantidad|adicional|
  |usuariopruebacr|1       |CostaRica    |00006   |6       |2        |
  |usuariopruebamx|1       |Mexico       |00006   |6       |2        |
  |usuariopruebapa|1       |Panama       |00006   |6       |2        |
  |usuariopruebapr|1       |PuertoRico   |00006   |6       |2        |
