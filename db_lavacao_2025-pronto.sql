create database if not exists db_lavacao_2025;
use db_lavacao_2025;

create table cor(
	id int not null auto_increment,
	nome varchar(50) not null,
	constraint pk_cor primary key(id)
) engine = InnoDB;

insert into cor(nome) value ("Vermelho");
insert into cor(nome) value ("Azul");
select * from cor;

create table marca(
	id int not null auto_increment,
	nome varchar(50) not null,
	constraint pk_marca primary key(id)
) engine = InnoDB;

insert into marca(nome) value ("Volkswagen");
insert into marca(nome) value ("Chevrolet");
select * from marca;

create table servico(
	id int not null auto_increment,
	descricao varchar(50) not null,
	valor double not null,
	pontos int default 10,
	categoria ENUM('TODAS', 'PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL default 'PADRAO',
	constraint pk_servico primary key(id)
) engine = InnoDB;

insert into servico(descricao, valor) values("Lavação Simples", 80.00);
insert into servico(descricao, valor) values("Lavação Com Cera", 100.00);
select * from servico;

create table modelo(
	id int not null auto_increment,
	descricao varchar(50) not null,
	id_marca int not null,
	categoria ENUM('PEQUENO', 'MÉDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL default 'PADRAO',
	constraint pk_modelo primary key(id),
	constraint fk_modelo_marca foreign key(id_marca) references marca(id)
) engine = InnoDB;

create table motor(
	id_modelo int not null references modelo(id),
	potencia int,
	combustivel enum('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') not null default'OUTRO',
	constraint pk_motor primary key(id_modelo),
	constraint fk_motor_modelo foreign key(id_modelo) references modelo(id) on delete cascade on update cascade
) engine = InnoDB;

insert into modelo(descricao, id_marca) values("Fox", 1);
insert into motor(id_modelo) (select max(id) from modelo);
insert into modelo(descricao, id_marca) values("Onix", 2);
insert into motor(id_modelo) (select max(id) from modelo);
select * from motor;
select * from modelo;

create table cliente(
	id int not null auto_increment,
	nome varchar(200) not null,
    celular varchar(15) not null,
    email varchar(50) not null,
    data_cadastro date not null,
    constraint pk_cliente primary key(id)
) engine = InnoDB;

create table pessoa_fisica(
	id_cliente int not null references cliente(id),
    cpf varchar(11) not null,
    data_nascimento date not null,
    constraint pk_pessoa_fisica primary key(id_cliente),
	constraint fk_pessoa_fisica_cliente foreign key(id_cliente) references cliente(id) on delete cascade on update cascade
) engine = InnoDB;

create table pessoa_juridica(
	id_cliente int not null references cliente(id),
    cnpj varchar(20) not null,
    inscricao_estadual varchar(50) not null,
    constraint pk_pessoa_juridica primary key(id_cliente),
    constraint fk_pessoa_juridica_cliente foreign key(id_cliente) references cliente(id) on delete cascade on update cascade
) engine = InnoDB;

insert into cliente(nome, celular, email, data_cadastro) values("Ana Moura", "48988888888", "anamoura@gmail.com","2025-06-10");
insert into pessoa_fisica(id_cliente, cpf, data_nascimento) values((SELECT max(id) FROM cliente), "11111111100","1990-05-03");
insert into cliente(nome, celular, email, data_cadastro) values("IFSC", "48999999999", "ifsc@gmail.com","2025-06-11");
insert into pessoa_juridica(id_cliente, cnpj, inscricao_estadual) values((SELECT max(id) FROM cliente), "11402887000241", "062244911.00-96");

select * from pessoa_fisica;
select * from pessoa_juridica;
select * from cliente;
SELECT * FROM cliente c left join pessoa_fisica pf on pf.id_cliente = c.id left join pessoa_juridica pj on pj.id_cliente = c.id ;

create table veiculo(
	id int not null auto_increment,
	placa varchar(7) not null,
	observacoes varchar(500),
	id_cor int not null,
	id_modelo int not null,
    id_cliente int not null,
	constraint pk_veiculo primary key(id),
	constraint fk_veiculo_cor foreign key(id_cor) references cor(id),
	constraint fk_veiculo_modelo foreign key(id_modelo) references modelo(id),
    constraint fk_veiculo_cliente foreign key(id_cliente) references cliente(id)
) engine = InnoDB;

insert into veiculo(placa, observacoes, id_cor, id_modelo, id_cliente) values("MDS1969","porta dianteira direita riscada", 2, 1, 1);
insert into veiculo(placa, observacoes, id_cor, id_modelo, id_cliente) values("ABC1234","parachoque rachado", 1, 2, 2);
select * from veiculo;