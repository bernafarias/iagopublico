package br.com.sgdw.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class Redirect {

	@CrossOrigin(methods = {RequestMethod.GET})
	@RequestMapping(method = RequestMethod.GET, path="/documentacao")
	public ModelAndView redirectDocumentation(){
		return new ModelAndView("redirect:/documentacao/index.html");
	}
}
