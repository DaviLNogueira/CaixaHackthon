FROM mcr.microsoft.com/mssql/server:2022-latest

USER root
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# Copia o script SQL para inicialização


# Permite execução de scripts
USER mssql
