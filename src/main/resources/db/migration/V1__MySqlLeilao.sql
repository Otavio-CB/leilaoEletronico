CREATE TABLE IF NOT EXISTS cliente
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    nome  VARCHAR(255),
    email VARCHAR(255),
    senha VARCHAR(255),
    CONSTRAINT pk_cliente PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS dispositivo
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    descricao    VARCHAR(255),
    valor_inicial DECIMAL(15, 2),
    vendido      BOOLEAN,
    leilao_id    BIGINT,
    nome         VARCHAR(255),
    tipo         VARCHAR(255),
    CONSTRAINT pk_dispositivo PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS instituicaofinanceira
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    nome VARCHAR(255),
    cnpj VARCHAR(255),
    CONSTRAINT pk_instituicaofinanceira PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS lance
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    valor      DECIMAL(15, 2),
    cliente_id BIGINT,
    leilao_id  BIGINT,
    CONSTRAINT pk_lance PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS leilao
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    data_ocorrencia TIMESTAMP,
    data_visita     TIMESTAMP,
    endereco       VARCHAR(255),
    cidade         VARCHAR(255),
    estado         VARCHAR(255),
    CONSTRAINT pk_leilao PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS leilao_dispositivo
(
    Leilao_id       BIGINT NOT NULL,
    dispositivos_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS leilao_veiculo
(
    Leilao_id   BIGINT NOT NULL,
    veiculos_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS veiculo
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    descricao    VARCHAR(255),
    valor_inicial DECIMAL(15, 2),
    vendido      BOOLEAN,
    leilao_id    BIGINT,
    modelo       VARCHAR(255),
    marca        VARCHAR(255),
    tipo         VARCHAR(255),
    CONSTRAINT pk_veiculo PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS leilao_instituicao_financeira
(
    instituicao_financeira_id BIGINT NOT NULL,
    leilao_id                 BIGINT NOT NULL
);

ALTER TABLE instituicaofinanceira
    ADD CONSTRAINT uc_instituicaofinanceira_cnpj UNIQUE (cnpj);

ALTER TABLE leilao_dispositivo
    ADD CONSTRAINT uc_leilao_dispositivos_dispositivos UNIQUE (dispositivos_id);

ALTER TABLE leilao_veiculo
    ADD CONSTRAINT uc_leilao_veiculos_veiculos UNIQUE (veiculos_id);

ALTER TABLE dispositivo
    ADD CONSTRAINT FK_DISPOSITIVO_ON_LEILAO FOREIGN KEY (leilao_id) REFERENCES leilao (id);

ALTER TABLE lance
    ADD CONSTRAINT FK_LANCE_ON_CLIENTE FOREIGN KEY (cliente_id) REFERENCES cliente (id);

ALTER TABLE lance
    ADD CONSTRAINT FK_LANCE_ON_LEILAO FOREIGN KEY (leilao_id) REFERENCES leilao (id);

ALTER TABLE veiculo
    ADD CONSTRAINT FK_VEICULO_ON_LEILAO FOREIGN KEY (leilao_id) REFERENCES leilao (id);

ALTER TABLE leilao_dispositivo
    ADD CONSTRAINT fk_leidis_on_dispositivo FOREIGN KEY (dispositivos_id) REFERENCES dispositivo (id);

ALTER TABLE leilao_dispositivo
    ADD CONSTRAINT fk_leidis_on_leilao FOREIGN KEY (Leilao_id) REFERENCES leilao (id);

ALTER TABLE leilao_instituicao_financeira
    ADD CONSTRAINT fk_leiinsfin_on_instituicao_financeira FOREIGN KEY (instituicao_financeira_id) REFERENCES instituicaofinanceira (id);

ALTER TABLE leilao_instituicao_financeira
    ADD CONSTRAINT fk_leiinsfin_on_leilao FOREIGN KEY (leilao_id) REFERENCES leilao (id);

ALTER TABLE leilao_veiculo
    ADD CONSTRAINT fk_leivei_on_leilao FOREIGN KEY (Leilao_id) REFERENCES leilao (id);

ALTER TABLE leilao_veiculo
    ADD CONSTRAINT fk_leivei_on_veiculo FOREIGN KEY (veiculos_id) REFERENCES veiculo (id);
