INSERT INTO usuarios (nombre, correo, clave_hash, rol, activo) VALUES
  ('Admin API', 'admin@aerovia.test', '$2a$12$mgCJYGP39flkAAW4x6aPFuTtrUqfYNAT1YjGc/PC0f4kTfavmi.fm', 'ADMIN', true),
  ('Agente API', 'agente@aerovia.test', '$2a$12$mgCJYGP39flkAAW4x6aPFuTtrUqfYNAT1YjGc/PC0f4kTfavmi.fm', 'AGENTE', true),
  ('Cliente Demo', 'cliente@aerovia.test', '$2a$12$mgCJYGP39flkAAW4x6aPFuTtrUqfYNAT1YjGc/PC0f4kTfavmi.fm', 'CLIENTE', true);

INSERT INTO aerolineas (nombre, codigo_iata) VALUES
  ('AeroVia Express', 'AVX'),
  ('Cielo Azul', 'CAZ');

INSERT INTO aeropuertos (codigo_iata, nombre, ciudad, pais) VALUES
  ('SAL', 'Monsenor Romero', 'San Salvador', 'El Salvador'),
  ('MIA', 'Miami International', 'Miami', 'Estados Unidos'),
  ('PTY', 'Aeropuerto de Tocumen', 'Ciudad de Panama', 'Panama');

INSERT INTO rutas (origen_id, destino_id, distancia_km) VALUES
  (1, 2, 1646),
  (1, 3, 1131);

INSERT INTO aviones (matricula, modelo, capacidad_total) VALUES
  ('YS-AX1', 'Airbus A320', 180),
  ('YS-BX2', 'Boeing 737-800', 189);

INSERT INTO clases (nombre, descripcion) VALUES
  ('ECONOMY', 'Clase economica'),
  ('BUSINESS', 'Clase ejecutiva');

INSERT INTO tarifa (codigo, clase_id, reembolsable) VALUES
  ('ECO-FLEX', 1, true),
  ('BUS-PREMIUM', 2, true);

INSERT INTO asientos_avion (avion_id, codigo_asiento, clase_id) VALUES
  (1, '12A', 1),
  (1, '12B', 1),
  (1, '1A', 2),
  (2, '15C', 1);

INSERT INTO vuelo (numero_vuelo, aerolinea_id, ruta_id, duracion_min) VALUES
  ('AVX101', 1, 1, 195),
  ('CAZ501', 2, 2, 140);

INSERT INTO operacion_vuelo (vuelo_id, avion_id, fecha_salida, fecha_llegada, estado) VALUES
  (1, 1, '2025-11-15T08:30:00', '2025-11-15T11:45:00', 'PROGRAMADO'),
  (2, 2, '2025-11-16T09:15:00', '2025-11-16T11:35:00', 'PROGRAMADO');

INSERT INTO tarifa_operacion (operacion_id, tarifa_id, precio, asientos_disponibles, version) VALUES
  (1, 1, 320.00, 50, 0),
  (1, 2, 680.00, 10, 0),
  (2, 1, 280.00, 60, 0),
  (2, 2, 620.00, 12, 0);

INSERT INTO tripulantes (nombre, tipo) VALUES
  ('Carlos Lopez', 'PILOTO'),
  ('Ana Torres', 'TCP');

INSERT INTO operacion_tripulacion (operacion_id, tripulante_id, rol_en_vuelo) VALUES
  (1, 1, 'PILOTO'),
  (1, 2, 'TRIPULANTE CABINA');

INSERT INTO pasajeros (nombre_completo, fecha_nacimiento, nro_pasaporte) VALUES
  ('Juan Perez', '1990-05-20', 'P9988776'),
  ('Maria Martinez', '1988-09-12', 'P8877665'),
  ('Luis Gomez', '1995-01-30', 'P7766554');

INSERT INTO reservas (codigo_reserva, usuario_id, fecha_reserva, estado, total) VALUES
  ('ABC123', 3, '2025-10-20T14:20:00', 'ACTIVA', 640.00);

INSERT INTO reserva_asiento (reserva_id, pasajero_id, operacion_id, asiento_avion_id, tarifa_oper_id, precio_pagado) VALUES
  (1, 1, 1, 1, 1, 320.00),
  (1, 2, 1, 2, 1, 320.00);

INSERT INTO pagos (reserva_id, fecha_pago, metodo_pago, monto) VALUES
  (1, '2025-10-20T14:25:00', 'TARJETA', 640.00);

INSERT INTO reclamos (reserva_id, pasajero_id, descripcion, estado) VALUES
  (1, 2, 'Retraso en la entrega del equipaje.', 'EN_PROCESO');




