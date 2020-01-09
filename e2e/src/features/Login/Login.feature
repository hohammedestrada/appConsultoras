Feature: Ingresar al app de Consultoras
  Como una Consultora
  Yo quiero ingresar al app Esik / Lbel Conmigo
  para realizar mis pedidos

@Login @LoginExitosoe
Scenario Outline:  Login exitoso en ESIKA
  Given He seleccionado el "<Pais>" ingreso el "<Usuario>" y "<Password>"
  When click en el boton "INICIAR SESION"
  Then Si los datos son correctos Ingreso al APP de Consultoras, sino muestra el "<Mensaje>"

Examples:
  |Pais          |Usuario        |Password  |Mensaje|
  |Bolivia       |usuariopruebabo|1234567   |Null   |
  |Chile         |usuariopruebacl|1234567|Null   |
  |Colombia      |usuariopruebaco|1234567|Null   |
 # |Ecuador       |usuariopruebaec|1234567|Null   |
 # |ElSalvador    |usuariopruebasv|1234567|Null   |
 # |Guatemala     |usuariopruebagt|1234567|Null   |
 # |Peru          |usuariopruebape|1234567|Null   |
 # |RepDominicana |usuariopruebado|1234567|Null   |

@Login @Lbel @LoginExitoso
Scenario Outline:  Login exitoso en LBEL
  Given He seleccionado el "<Pais>" ingreso el "<Usuario>" y "<Password>"
  When click en el boton "INICIAR SESION"
  Then Si los datos son correctos Ingreso al APP de Consultoras, sino muestra el "<Mensaje>"

Examples:
  |Pais      |Usuario  |Password|Mensaje|
  |CostaRica |0005455  |1234567      |Null   |
  |Mexico    |0019473  |1234567      |Null   |
  |Panama    |200759427|1234567      |Null   |
  |PuertoRico|0287499  |1234567      |Null   |

@Login @FallidoSinPWDe
Scenario Outline:  Login Fallido usuario sin contraseña en ESIKA
  Given He seleccionado el "<Pais>" ingreso el "<Usuario>" y "<Password>"
  When click en el boton "INICIAR SESION"
  Then Si los datos son correctos Ingreso al APP de Consultoras, sino muestra el "<Mensaje>"

Examples:
  |Pais          |Usuario   |Password|Mensaje                      |
  |Bolivia       |0022785   |        |Debes ingresar tu Contraseña.|
  |Chile         |188851835 |        |Debes ingresar tu Contraseña.|
  |Colombia      |0020143905|        |Debes ingresar tu Contraseña.|
  |Ecuador       |1712905809|        |Debes ingresar tu Contraseña.|
  |ElSalvador    |0101262   |        |Debes ingresar tu Contraseña.|
  |Guatemala     |0108732   |        |Debes ingresar tu Contraseña.|
  |Peru          |044459523 |        |Debes ingresar tu Contraseña.|
  |RepDominicana |0002016   |        |Debes ingresar tu Contraseña.|

@login @Lbel @FallidoSinPWD
Scenario Outline:  Login Fallido usuario sin contraseña en LBEL
  Given He seleccionado el "<Pais>" ingreso el "<Usuario>" y "<Password>"
  When click en el boton "INICIAR SESION"
  Then Si los datos son correctos Ingreso al APP de Consultoras, sino muestra el "<Mensaje>"

Examples:
  |Pais      |Usuario  |Password|Mensaje                      |
  |CostaRica |0005455  |        |Debes ingresar tu Contraseña.|
  |Mexico    |0019473  |        |Debes ingresar tu Contraseña.|
  |Panama    |200759427|        |Debes ingresar tu Contraseña.|
  |PuertoRico|0287499  |        |Debes ingresar tu Contraseña.|

@Login @FallidoSinUsuarioe
Scenario Outline:  Login Fallido contraseña sin Usuario en ESIKA
  Given He seleccionado el "<Pais>" ingreso el "<Usuario>" y "<Password>"
  When  click en el boton "INICIAR SESION"
  Then  Si los datos son correctos Ingreso al APP de Consultoras, sino muestra el "<Mensaje>"

Examples:
  |Pais          |Usuario   |Password |Mensaje                   |
  |Bolivia       |          |1234567       |Debes ingresar tu Usuario.|
  |Chile         |          |1234567       |Debes ingresar tu Usuario.|
  |Colombia      |          |1234567       |Debes ingresar tu Usuario.|
  |Ecuador       |          |1234567       |Debes ingresar tu Usuario.|
  |ElSalvador    |          |1234567       |Debes ingresar tu Usuario.|
  |Guatemala     |          |1234567       |Debes ingresar tu Usuario.|
  |Peru          |          |1234567       |Debes ingresar tu Usuario.|
  |RepDominicana |          |1234567       |Debes ingresar tu Usuario.|

@Login @Lbel @FallidoSinUsuario
Scenario Outline:  Login Fallido contraseña sin Usuario en LBEL
  Given He seleccionado el "<Pais>" ingreso el "<Usuario>" y "<Password>"
  When click en el boton "INICIAR SESION"
  Then Si los datos son correctos Ingreso al APP de Consultoras, sino muestra el "<Mensaje>"

Examples:
  |Pais      |Usuario  |Password|Mensaje                   |
  |CostaRica |         |1234567      |Debes ingresar tu Usuario.|
  |Mexico    |         |1234567      |Debes ingresar tu Usuario.|
  |Panama    |         |1234567      |Debes ingresar tu Usuario.|
  |PuertoRico|         |1234567      |Debes ingresar tu Usuario.|

@Login @FallidoPWDIncorrectoe
Scenario Outline:  Login Fallido Usuario o Contraseña Incorrecta en ESIKA
  Given He seleccionado el "<Pais>" ingreso el "<Usuario>" y "<Password>"
  When click en el boton "INICIAR SESION"
  Then Si los datos son correctos Ingreso al APP de Consultoras, sino muestra el "<Mensaje>"

Examples:
  |Pais          |Usuario   |Password  |Mensaje                          |
  |Bolivia       |0022785   |1234567       |Usuario o Contraseña Incorrectas.|
  |Chile         |188851835 |1234567       |Usuario o Contraseña Incorrectas.|
  |Colombia      |0020143905|1234567       |Usuario o Contraseña Incorrectas.|
  |Ecuador       |1712905809|1234567       |Usuario o Contraseña Incorrectas.|
  |ElSalvador    |0101262   |1234567       |Usuario o Contraseña Incorrectas.|
  |Guatemala     |0108732   |1234567       |Usuario o Contraseña Incorrectas.|
  |Peru          |044459523 |1234567       |Usuario o Contraseña Incorrectas.|
  |RepDominicana |0002016   |1234567       |Usuario o Contraseña Incorrectas.|

@Login @Lbel @FallidoPWDIncorrecto
Scenario Outline:  Login Fallido Usuario o Contraseña Incorrecta en LBEL
  Given He seleccionado el "<Pais>" ingreso el "<Usuario>" y "<Password>"
  When click en el boton "INICIAR SESION"
  Then Si los datos son correctos Ingreso al APP de Consultoras, sino muestra el "<Mensaje>"

Examples:
  |Pais      |Usuario  |Password|Mensaje                          |
  |CostaRica |0005455  |1234567     |Usuario o Contraseña Incorrectas.|
  |Mexico    |0019473  |1234567     |Usuario o Contraseña Incorrectas.|
  |Panama    |200759427|1234567     |Usuario o Contraseña Incorrectas.|
  |PuertoRico|0287499  |1234567     |Usuario o Contraseña Incorrectas.|
