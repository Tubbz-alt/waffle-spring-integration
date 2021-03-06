package com.amazingrv.springwaffle.rest;

import com.amazingrv.springwaffle.constants.PathConstants;
import com.amazingrv.springwaffle.constants.ServiceConstants;
import com.amazingrv.springwaffle.dto.PersonDTO;
import com.amazingrv.springwaffle.service.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api("Operations for Person Entity")
@RequestMapping(PathConstants.API_PERSON)
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    private ResponseEntity<Object> getResponse(String key, Object result) {
        Map<String, Object> response = new HashMap<>();
        response.put(key, result);
        return ResponseEntity.ok(response);
    }

    @ApiOperation("Returns all persons")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllPersons(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        List<PersonDTO> persons = service.getAllPersons(pageable);
        return getResponse(ServiceConstants.PERSONS, persons);
    }

    @ApiOperation("Returns person by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPerson(@PathVariable("id") String id) {
        PersonDTO person = service.getPerson(id);
        return getResponse(ServiceConstants.PERSON, person);
    }

    @ApiOperation("Returns id after creating a person")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> savePersonToDB(@Valid @RequestBody PersonDTO person) {
        String userId = service.createPerson(person);
        return getResponse(ServiceConstants.USER_ID, userId);
    }

    @ApiOperation("Returns id after updating a person")
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editPersonInDB(@RequestBody PersonDTO person) {
        String userId = service.updatePerson(person);
        return getResponse(ServiceConstants.USER_ID, userId);
    }

    @ApiOperation("Returns id after removing a person")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deletePersonFromDB(@PathVariable String id) {
        String userId = service.deletePerson(id);
        return getResponse(ServiceConstants.USER_ID, userId);
    }
}
