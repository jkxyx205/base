package com.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControllerTest {
	@RequestMapping(value="/index")
	public String gotoIndex() {
		return "index";
	}
}
