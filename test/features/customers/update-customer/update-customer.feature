# language: es
Característica: Actualizar cliente

  Escenario: actualizar nombre de cliente
    Dado un cliente existente con id 1 y nombre Juan
    Cuando cambiamos el nombre de este cliente por John
    Entonces tras consultarlo otra vez, su nombre debe ser John
