package com.udb.aerovia_api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udb.aerovia_api.domain.enums.EstadoOperacionVuelo;
import com.udb.aerovia_api.domain.enums.EstadoReclamo;
import com.udb.aerovia_api.dto.AerolineaDto;
import com.udb.aerovia_api.dto.AeropuertoDto;
import com.udb.aerovia_api.dto.AsientoAvionDto;
import com.udb.aerovia_api.dto.AsignarTripulanteDto;
import com.udb.aerovia_api.dto.AvionDto;
import com.udb.aerovia_api.dto.CancelacionDto;
import com.udb.aerovia_api.dto.ClaseDto;
import com.udb.aerovia_api.dto.EstadisticasDto;
import com.udb.aerovia_api.dto.LoginRequestDto;
import com.udb.aerovia_api.dto.OperacionTripulacionDto;
import com.udb.aerovia_api.dto.OperacionVueloDto;
import com.udb.aerovia_api.dto.PagoDto;
import com.udb.aerovia_api.dto.PasajeroDto;
import com.udb.aerovia_api.dto.ReclamoDto;
import com.udb.aerovia_api.dto.ReservaAsientoDetalleDto;
import com.udb.aerovia_api.dto.ReservaAdminDto;
import com.udb.aerovia_api.dto.ReservaDetalleDto;
import com.udb.aerovia_api.dto.RutaDto;
import com.udb.aerovia_api.dto.TarifaDto;
import com.udb.aerovia_api.dto.TarifaOperacionDto;
import com.udb.aerovia_api.dto.TripulanteDto;
import com.udb.aerovia_api.dto.UserDto;
import com.udb.aerovia_api.dto.VueloDto;
import com.udb.aerovia_api.domain.enums.EstadoReserva;
import com.udb.aerovia_api.service.AerolineaService;
import com.udb.aerovia_api.service.AeropuertoService;
import com.udb.aerovia_api.service.AsientoAvionService;
import com.udb.aerovia_api.service.AvionService;
import com.udb.aerovia_api.service.CancelacionService;
import com.udb.aerovia_api.service.ClaseService;
import com.udb.aerovia_api.service.EstadisticasService;
import com.udb.aerovia_api.service.OperacionTripulacionService;
import com.udb.aerovia_api.service.OperacionVueloService;
import com.udb.aerovia_api.service.PagoService;
import com.udb.aerovia_api.service.PasajeroService;
import com.udb.aerovia_api.service.ReclamoService;
import com.udb.aerovia_api.service.ReservaService;
import com.udb.aerovia_api.service.RutaService;
import com.udb.aerovia_api.service.TarifaOperacionService;
import com.udb.aerovia_api.service.TarifaService;
import com.udb.aerovia_api.service.TripulanteService;
import com.udb.aerovia_api.service.UserService;
import com.udb.aerovia_api.service.VueloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ControllersIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AerolineaService aerolineaService;

    @MockBean
    private AeropuertoService aeropuertoService;

    @MockBean
    private AsientoAvionService asientoAvionService;

    @MockBean
    private AvionService avionService;

    @MockBean
    private CancelacionService cancelacionService;

    @MockBean
    private ClaseService claseService;

    @MockBean
    private EstadisticasService estadisticasService;

    @MockBean
    private OperacionTripulacionService operacionTripulacionService;

    @MockBean
    private OperacionVueloService operacionVueloService;

    @MockBean
    private PagoService pagoService;

    @MockBean
    private PasajeroService pasajeroService;

    @MockBean
    private ReclamoService reclamoService;

    @MockBean
    private ReservaService reservaService;

    @MockBean
    private RutaService rutaService;

    @MockBean
    private TarifaService tarifaService;

    @MockBean
    private TarifaOperacionService tarifaOperacionService;

    @MockBean
    private TripulanteService tripulanteService;

    @MockBean
    private VueloService vueloService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private com.udb.aerovia_api.security.JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListAerolineas() throws Exception {
        List<AerolineaDto> aerolineas = List.of(new AerolineaDto(1L, "Aerovia", "AV"));
        when(aerolineaService.obtenerTodasLasAerolineas()).thenReturn(aerolineas);

        mockMvc.perform(get("/api/v1/aerolineas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Aerovia"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListAeropuertos() throws Exception {
        List<AeropuertoDto> aeropuertos = List.of(new AeropuertoDto(1L, "SAL", "Monsenor Romero", "San Salvador", "El Salvador"));
        when(aeropuertoService.obtenerTodos()).thenReturn(aeropuertos);

        mockMvc.perform(get("/api/v1/aeropuertos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoIata").value("SAL"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListAsientosPorAvion() throws Exception {
        ClaseDto clase = new ClaseDto(1L, "Economy", "Clase economica");
        List<AsientoAvionDto> asientos = List.of(new AsientoAvionDto(1L, 10L, "1A", clase));
        when(asientoAvionService.obtenerAsientosPorAvion(10L)).thenReturn(asientos);

        mockMvc.perform(get("/api/v1/asientos-avion/avion/{avionId}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoAsiento").value("1A"));
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto("user@demo.com", "secret");
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.correo(), loginRequest.password());

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("mock-jwt-token");
        when(userService.getUserByEmail(any()))
                .thenReturn(new UserDto(42L, "Demo User", loginRequest.correo(), "ADMIN"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mock-jwt-token"))
                .andExpect(jsonPath("$.user.correo").value("user@demo.com"))
                .andExpect(jsonPath("$.user.rol").value("ADMIN"));
    }

    @Test
    @WithMockUser(username = "me@demo.com")
    void shouldReturnCurrentUser() throws Exception {
        when(userService.getUserByEmail("me@demo.com"))
                .thenReturn(new UserDto(99L, "Current User", "me@demo.com", "CLIENTE"));

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("me@demo.com"))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListAviones() throws Exception {
        List<AvionDto> aviones = List.of(new AvionDto(1L, "YS-100", "Boeing 737", 150));
        when(avionService.obtenerTodos()).thenReturn(aviones);

        mockMvc.perform(get("/api/v1/aviones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].matricula").value("YS-100"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListReservasAdmin() throws Exception {
        ReservaAdminDto reservaAdminDto = new ReservaAdminDto(
                1L,
                "ABC123",
                EstadoReserva.ACTIVA,
                LocalDateTime.now(),
                BigDecimal.valueOf(640.00),
                "Cliente Demo",
                "cliente@demo.com",
                "Aerovia Express",
                "San Salvador",
                "Miami");
        Page<ReservaAdminDto> page = new PageImpl<>(List.of(reservaAdminDto));
        when(reservaService.buscarReservasAdmin(any(Pageable.class), any(), any(), any(), any(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].codigoReserva").value("ABC123"));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void shouldRejectReservasAdminForClient() throws Exception {
        mockMvc.perform(get("/api/v1/reservas"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListPagosAdmin() throws Exception {
        PagoDto pagoDto = new PagoDto(1L, 3L, LocalDateTime.now(), "TARJETA", BigDecimal.valueOf(640.00));
        Page<PagoDto> page = new PageImpl<>(List.of(pagoDto));
        when(pagoService.buscarPagosAdmin(any(Pageable.class), any(), any(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].monto").value(640.00));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void shouldRejectPagosAdminForClient() throws Exception {
        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldReturnCancelacionPorReserva() throws Exception {
        CancelacionDto cancelacion = new CancelacionDto(1L, 5L, LocalDateTime.now(), BigDecimal.ZERO, BigDecimal.TEN);
        when(cancelacionService.obtenerCancelacionPorReservaId(5L)).thenReturn(cancelacion);

        mockMvc.perform(get("/api/v1/cancelaciones/reserva/{reservaId}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservaId").value(5L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListClases() throws Exception {
        List<ClaseDto> clases = List.of(new ClaseDto(1L, "Business", "Mayor comodidad"));
        when(claseService.obtenerTodas()).thenReturn(clases);

        mockMvc.perform(get("/api/v1/clases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Business"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnEstadisticas() throws Exception {
        EstadisticasDto estadisticas = new EstadisticasDto(5, 2, 1, 2);
        when(estadisticasService.obtenerEstadisticasBasicas()).thenReturn(estadisticas);

        mockMvc.perform(get("/api/v1/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReservasActivas").value(5));
    }

    @Test
    @WithMockUser(roles = "AGENTE")
    void shouldAsignarTripulante() throws Exception {
        TripulanteDto tripulanteDto = new TripulanteDto(3L, "Carlos Perez", "PILOTO");
        OperacionTripulacionDto respuesta = new OperacionTripulacionDto(1L, 7L, tripulanteDto, "PILOTO");
        when(operacionTripulacionService.asignarTripulante(any())).thenReturn(respuesta);

        AsignarTripulanteDto request = new AsignarTripulanteDto(7L, 3L, "PILOTO");

        mockMvc.perform(post("/api/v1/operaciones-tripulacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tripulante.nombre").value("Carlos Perez"));
    }

    @Test
    @WithMockUser
    void shouldListOperacionesVuelo() throws Exception {
        AeropuertoDto origen = new AeropuertoDto(1L, "SAL", "Monsenor Romero", "San Salvador", "El Salvador");
        AeropuertoDto destino = new AeropuertoDto(2L, "MIA", "Miami Intl", "Miami", "Estados Unidos");
        RutaDto ruta = new RutaDto(4L, origen, destino, 1920);
        AerolineaDto aerolinea = new AerolineaDto(5L, "Aerovia", "AV");
        VueloDto vuelo = new VueloDto(6L, "AV123", aerolinea, ruta, 180);
        AvionDto avion = new AvionDto(7L, "YS-200", "Airbus A320", 180);
        OperacionVueloDto operacion = new OperacionVueloDto(8L, vuelo, avion, LocalDateTime.now(), LocalDateTime.now().plusHours(3), EstadoOperacionVuelo.PROGRAMADO);

        when(operacionVueloService.obtenerTodas()).thenReturn(List.of(operacion));

        mockMvc.perform(get("/api/v1/operaciones-vuelo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vuelo.numeroVuelo").value("AV123"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListPagosPorReserva() throws Exception {
        PagoDto pago = new PagoDto(1L, 9L, LocalDateTime.now(), "TARJETA", BigDecimal.valueOf(199.99));
        when(pagoService.obtenerPagosPorReserva(9L)).thenReturn(List.of(pago));

        mockMvc.perform(get("/api/v1/pagos/reserva/{reservaId}", 9))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].metodoPago").value("TARJETA"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListPasajeros() throws Exception {
        PasajeroDto pasajero = new PasajeroDto(1L, "Laura Gomez", LocalDate.of(1995, 3, 15), "P12345", 1L);
        when(pasajeroService.obtenerTodos()).thenReturn(List.of(pasajero));

        mockMvc.perform(get("/api/v1/pasajeros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCompleto").value("Laura Gomez"));
    }

    @Test
    @WithMockUser
    void shouldListReclamosPorReserva() throws Exception {
        PasajeroDto pasajero = new PasajeroDto(2L, "Luis Martinez", LocalDate.of(1990, 1, 10), "P54321", 1L);
        ReclamoDto reclamo = new ReclamoDto(1L, 11L, pasajero, "Demora en vuelo", EstadoReclamo.ABIERTO);
        when(reclamoService.obtenerReclamosPorReserva(11L)).thenReturn(List.of(reclamo));

        mockMvc.perform(get("/api/v1/reclamos/reserva/{reservaId}", 11))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion").value("Demora en vuelo"));
    }

    @Test
    @WithMockUser
    void shouldObtenerReservaPorCodigo() throws Exception {
        ReservaDetalleDto reserva = buildReservaDetalleDto("ABC123");
        when(reservaService.obtenerReservaPorCodigo("ABC123")).thenReturn(reserva);

        mockMvc.perform(get("/api/v1/reservas/{codigoReserva}", "ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoReserva").value("ABC123"));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void shouldListMisReservas() throws Exception {
        when(reservaService.obtenerMisReservas()).thenReturn(List.of(buildReservaDetalleDto("XYZ789")));

        mockMvc.perform(get("/api/v1/reservas/mis-reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoReserva").value("XYZ789"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListRutas() throws Exception {
        AeropuertoDto origen = new AeropuertoDto(1L, "SAL", "Monsenor Romero", "San Salvador", "El Salvador");
        AeropuertoDto destino = new AeropuertoDto(2L, "PTY", "Tocumen", "Panama", "Panama");
        RutaDto ruta = new RutaDto(1L, origen, destino, 1300);
        when(rutaService.obtenerTodas()).thenReturn(List.of(ruta));

        mockMvc.perform(get("/api/v1/rutas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].origen.codigoIata").value("SAL"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListTarifas() throws Exception {
        ClaseDto clase = new ClaseDto(1L, "Economy", "Standard");
        TarifaDto tarifa = new TarifaDto(1L, "ECO", clase, true);
        when(tarifaService.obtenerTodas()).thenReturn(List.of(tarifa));

        mockMvc.perform(get("/api/v1/tarifas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("ECO"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListTarifasOperacion() throws Exception {
        ClaseDto clase = new ClaseDto(1L, "Economy", "Standard");
        TarifaDto tarifa = new TarifaDto(1L, "ECO", clase, true);
        TarifaOperacionDto tarifaOperacion = new TarifaOperacionDto(1L, 15L, tarifa, BigDecimal.valueOf(150.0), 20);
        when(tarifaOperacionService.obtenerTarifasPorOperacion(15L)).thenReturn(List.of(tarifaOperacion));

        mockMvc.perform(get("/api/v1/tarifas-operacion/operacion/{operacionId}", 15))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].asientosDisponibles").value(20));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListTripulantes() throws Exception {
        TripulanteDto tripulante = new TripulanteDto(1L, "Ana Torres", "TCP");
        when(tripulanteService.obtenerTodos()).thenReturn(List.of(tripulante));

        mockMvc.perform(get("/api/v1/tripulantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("TCP"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListVuelos() throws Exception {
        AerolineaDto aerolinea = new AerolineaDto(1L, "Aerovia", "AV");
        AeropuertoDto origen = new AeropuertoDto(1L, "SAL", "Monsenor Romero", "San Salvador", "El Salvador");
        AeropuertoDto destino = new AeropuertoDto(2L, "LAX", "Los Angeles Intl", "Los Angeles", "Estados Unidos");
        RutaDto ruta = new RutaDto(1L, origen, destino, 3000);
        VueloDto vuelo = new VueloDto(1L, "AV201", aerolinea, ruta, 240);
        when(vueloService.obtenerTodos()).thenReturn(List.of(vuelo));

        mockMvc.perform(get("/api/v1/vuelos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroVuelo").value("AV201"));
    }

    private ReservaDetalleDto buildReservaDetalleDto(String codigo) {
        UserDto usuario = new UserDto(1L, "Cliente Demo", "cliente@demo.com", "CLIENTE");
        ClaseDto clase = new ClaseDto(1L, "Economy", "Standard");
        AeropuertoDto origen = new AeropuertoDto(1L, "SAL", "Monsenor Romero", "San Salvador", "El Salvador");
        AeropuertoDto destino = new AeropuertoDto(2L, "GUA", "La Aurora", "Ciudad de Guatemala", "Guatemala");
        RutaDto ruta = new RutaDto(1L, origen, destino, 200);
        AerolineaDto aerolinea = new AerolineaDto(1L, "Aerovia", "AV");
        VueloDto vuelo = new VueloDto(1L, "AV100", aerolinea, ruta, 60);
        AvionDto avion = new AvionDto(1L, "YS-001", "ATR 42", 50);
        OperacionVueloDto operacion = new OperacionVueloDto(1L, vuelo, avion, LocalDateTime.now(), LocalDateTime.now().plusHours(1), EstadoOperacionVuelo.PROGRAMADO);

        PasajeroDto pasajero = new PasajeroDto(1L, "Juan Perez", LocalDate.of(1990, 5, 20), "P9988", 1L);
        AsientoAvionDto asiento = new AsientoAvionDto(1L, avion.id(), "1A", clase);
        TarifaDto tarifa = new TarifaDto(1L, "ECO", clase, true);
        ReservaAsientoDetalleDto asientoDetalle = new ReservaAsientoDetalleDto(1L, pasajero, asiento, tarifa, BigDecimal.valueOf(120.0));

        return new ReservaDetalleDto(
                1L,
                codigo,
                usuario,
                LocalDateTime.now(),
                EstadoReserva.ACTIVA,
                BigDecimal.valueOf(120.0),
                operacion,
                List.of(asientoDetalle)
        );
    }
}





