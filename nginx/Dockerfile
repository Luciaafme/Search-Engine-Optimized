# Usa la imagen oficial de NGINX como base
FROM nginx:latest

# Copia el archivo de configuración personalizado a la imagen
COPY nginx.conf /etc/nginx/nginx.conf

# Exponer el puerto 80 para las conexiones HTTP
EXPOSE 80

# Comando para iniciar NGINX
CMD ["nginx", "-g", "daemon off;"]
