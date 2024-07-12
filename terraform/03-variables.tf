# Объявление переменных конфигурации
variable "aws_region" {
  description = "Регион AWS"
  type = string
  default = "eu-west-1"
}

variable "aws_ami" {
  description = "Имя образа ami"
  type = string
  #Образ Canonical, Ubuntu, 22.04 LTS, amd64 jammy image build on 2024-04-11
  default = "ami-0607a9783dd204cae"
}

variable "aws_instance" {
  description = "Тип инстанса"
  type = string
  default = "t2.micro"
}

variable "key_name" {
  description = "Имя приватного ключа"
  type = string
  default = "tf_key.pem"
}