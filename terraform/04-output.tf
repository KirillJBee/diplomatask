output "public_ip" {
 value       = aws_instance.public_instance.public_ip
 description = "Публичный IP адрес созданнго инстанса EC2"
}

output "instance_id" {
 value       = aws_instance.public_instance.id
 description = "Инстанс ID"
}