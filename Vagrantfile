# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  config.vm.define "db" do |db|
    db.vm.provider "docker" do |d|
      d.name = "mysql-uuid-benchmark-db"
      d.image = "mysql:5.7"
      d.vagrant_machine = "docker-host"
      d.vagrant_vagrantfile = "./Vagrantfile_host"
      d.volumes = [
        "mysql-uuid-data:/var/lib/mysql"
      ]
      d.ports = ["3306:3306"]
      d.remains_running = true
      d.create_args = ["-m", "2GB"]
      d.env = {
        "MYSQL_ROOT_PASSWORD": "mysql"
      }
    end
  end
end
