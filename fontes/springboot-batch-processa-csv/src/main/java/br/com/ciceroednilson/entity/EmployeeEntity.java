package br.com.ciceroednilson.entity;

public class EmployeeEntity {

	public EmployeeEntity(){
		
	}
	
	public EmployeeEntity(Integer code, String name, float salary) {
		super();
		this.code = code;
		this.name = name;
		this.salary = salary;
	}
	
	private Integer code;
	private String  name;
	private float  salary;
	
	public Integer getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public float getSalary() {
		return salary;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
	
	
	
}
