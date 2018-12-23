package br.com.ciceroednilson.process;

import org.springframework.batch.item.ItemProcessor;

import br.com.ciceroednilson.entity.EmployeeEntity;

public class EmployeeItemProcessor implements ItemProcessor<EmployeeEntity, EmployeeEntity> {

	@Override
	public EmployeeEntity process(EmployeeEntity emp) throws Exception {
		
		//APENAS COLOCA O NAME EM MAIÚSCULO.
		return new EmployeeEntity(emp.getCode(), emp.getName().toUpperCase(), emp.getSalary());
	}
 
}
