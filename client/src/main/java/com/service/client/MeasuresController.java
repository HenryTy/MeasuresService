package com.service.client;


import com.service.client.model.MeasuresRequest;
import com.service.client.model.MeasuresResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class MeasuresController {

    @org.springframework.beans.factory.annotation.Value("${measures.service.server}")
    private String measuresServiceServer;

    @GetMapping("/request")
    public String request(Model model) {
        model.addAttribute("measuresRequest", new MeasuresRequest());
        return "request";
    }

    @PostMapping("/download")
    public String download(@ModelAttribute MeasuresRequest measuresRequest, RestTemplate restTemplate, Model model) {
        ResponseEntity<MeasuresResponse> re = restTemplate.postForEntity(
                "http://" + measuresServiceServer + "/api/measures/download", measuresRequest, MeasuresResponse.class);
        model.addAttribute("measuresId", re.getBody().getMeasuresId());
        return "status";
    }

    @PostMapping("/view/{measuresId}")
    public String view(@PathVariable("measuresId") String measuresId, RestTemplate restTemplate, Model model) {
        ResponseEntity<MeasuresResponse> re = restTemplate.getForEntity(
                "http://" + measuresServiceServer + "/api/measures/view/" + measuresId, MeasuresResponse.class);
        MeasuresResponse measuresResponse = re.getBody();
        if(measuresResponse.getErrorMessage() != null) {
            model.addAttribute("error", true);
        }
        else {
            model.addAttribute("measures", measuresResponse.getMeasures());
        }
        return "result";
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
