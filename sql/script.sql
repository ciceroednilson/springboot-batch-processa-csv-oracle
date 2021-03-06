--CRIANDO O USUÁRIO
CREATE USER cicero IDENTIFIED BY 123456;


--CRIANDO A TABELA
CREATE TABLE CICERO.TB_FUNCIONARIOS(
    CODIGO  NUMBER(38,0),
    NOME    VARCHAR2(60 BYTE),
    SALARIO NUMBER(13,2)    
)


--ADICIONANDO A CONSTRAINT
ALTER TABLE TB_FUNCIONARIOS ADD ( CONSTRAINT TB_FUNCIONARIOS_CODIGO PRIMARY KEY (CODIGO));


--CRIANDO A SEQUENCE
CREATE SEQUENCE seq_funcionario
MINVALUE 1           /*VALOR MINIMO */
MAXVALUE 9999999999 /*VALOR MÁXIMO */
START WITH 1        /*VALOR INICIAL*/
INCREMENT BY 1;    /*INCREMENTA DE 1 EM 1 */





--CRIANDO A TRIGGER PARA AUTO INSERT DO CÓDIGO(CHAVE DA TABELA)
CREATE OR REPLACE TRIGGER tr_insert_codigo_funcionario
                  BEFORE INSERT ON TB_FUNCIONARIOS FOR EACH ROW       
BEGIN

SELECT seq_funcionario.NEXTVAL
INTO :new.CODIGO
FROM DUAL;
END;
