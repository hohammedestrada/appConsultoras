@AgregarPorCUV
Feature: Agregar un producto al pase de pedido mediante el buscador del home
  Como una Consultora quiero agregar producto desde el buscador del home
  Agregar Producto por cuv 'CUV'
  Agregar Producto por descripcion 'DESC'

Scenario Outline: Agregar producto por CUV al pedido desde el buscador
  Given Con el "<Usuario>" y "<Password>" del pais "<Pais>", ingreso al buscador
  When Ingreso el "<Valor>"
  And Se busca por "<Tipo>" y agrego la "<Cantidad>" para agregar al pedido
  And Terminada la operacion hago clic en "Agregar"
  And Regreso al Home e ingreso al pase de pedido
  Then Se muestra el producto agregado desde el buscador

Examples:
  |Usuario        |Password|Pais   |Valor|Tipo|Cantidad|
  |usuariopruebape|1234567 |Peru   |49318|CUV |2       |
