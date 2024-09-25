package com.example.patitas.controller;

import com.example.patitas.config.RestTemplateConfig;
import com.example.patitas.dto.RequestLogin;
import com.example.patitas.dto.ResponseLogin;
import com.example.patitas.viewmodel.LoginModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@Controller

@RequestMapping("/login")
public class LoginController {

  @Autowired
  RestTemplate restTemplate;

  @GetMapping("/inicio")
  public String inicio(Model model){

    LoginModel loginmodel = new LoginModel("00","","Pepe");
    model.addAttribute("loginmodel",loginmodel);

    return "inicio";
  }

  @PostMapping("/autenticar")
  public String autenticar(@RequestParam("tipoDocumento") String tipodocumento,
                           @RequestParam("numeroDocumento") String numerodocumento,
                           @RequestParam("password")String password,
                           Model model){

    //TAREA: Creamos la variable para enviar los datos al front
    LoginModel loginmodel;

    //Hecho en clase (mas optimo)

    //Validamos los campos
    if (tipodocumento == null || tipodocumento.trim().length() == 0 ||
    numerodocumento == null || numerodocumento.trim().length() == 0 ||
      password == null || password.trim().length() == 0){

      //En caso no ingresen todos los datos, salta este error
      loginmodel = new LoginModel("01","Datos insuficientes","");
      model.addAttribute("loginmodel",loginmodel);
      return "inicio";
    }

    // Hacemos el consumo de la API

    //NOTA: Esto fue hecho para la tarea. Mas abajo esta el codigo de la clase que es mas optimo
    /*
    try {
      URL url = new URL("http://localhost:8081/autenticacion/login");
      HttpURLConnection cnn = (HttpURLConnection)url.openConnection();
      cnn.setRequestMethod("POST");
      cnn.setRequestProperty("Content-Type", "application/json");
      cnn.setDoOutput(true);

      // Crear el JSON manualmente (queria usar una libreria pero no me aparecian las clases)
      String jsonInputString = String.format(
        "{\"tipoDocumento\": \"%s\", \"numeroDocumento\": \"%s\", \"password\": \"%s\"}",
        tipodocumento, numerodocumento, password
      );

      // Enviar el JSON en el cuerpo de la solicitud
      try (OutputStream os = cnn.getOutputStream()) {
        byte[] input = jsonInputString.getBytes("UTF-8");
        os.write(input, 0, input.length);
      }

      //CONECTAMOOOOOOOOOOOOS
      cnn.connect();

      //Si hay algún error...
      if (cnn.getResponseCode() != 200) {
        loginmodel = new LoginModel("02","","Error de API");
        model.addAttribute("loginmodel",loginmodel);
        return "inicio";
      }

      //Ahora leemos la respuesta
      InputStreamReader in = new InputStreamReader(cnn.getInputStream());
      BufferedReader br = new BufferedReader(in);
      String output;
      StringBuilder response = new StringBuilder();
      while ((output = br.readLine()) != null) {
        response.append(output);
        System.out.println(response);
      }

      // Convertir la respuesta JSON a nuestro dto
      ObjectMapper objectMapper = new ObjectMapper();
      ResponseLogin loginResponse = objectMapper.readValue(response.toString(), ResponseLogin.class);

      //ingresamos la info necesaria en el model
      loginmodel = new LoginModel(loginResponse.codigo(),loginResponse.mensaje(),loginResponse.nombreUsuario());
      cnn.disconnect();

    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
*/
    //LoginModel loginmodel = new LoginModel("00","","Jorge Torres");

    try {
      String url = "http://localhost:8081/autenticacion/login";
      RequestLogin request = new RequestLogin(tipodocumento, numerodocumento, password);
      ResponseLogin response = restTemplate.postForObject(
        url, request, ResponseLogin.class
      );

      if(response.codigo().equals("00")){
        loginmodel = new LoginModel("00",
          "",response.nombreUsuario());
        model.addAttribute("loginmodel",loginmodel);
        return "principal";
      }
      else {
        loginmodel = new LoginModel("01","Datos insuficientes","");
        model.addAttribute("loginmodel",loginmodel);
        return "inicio";
      }

    }catch (Exception e){
      loginmodel = new LoginModel("99","Servicio no responde","");
      model.addAttribute("loginmodel",loginmodel);
      System.out.println(e.getMessage());
      return "inicio";
    }

  }

}
