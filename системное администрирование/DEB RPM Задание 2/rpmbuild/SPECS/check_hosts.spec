Name:          check_hosts
Version:       1.0
Release:       1%{?dist}
Summary:       Скрипт для проверки доступности хостов
Group:         Utilities
License:       GPL
URL:           https://example.com
Source0:       %{name}-%{version}.tar.gz
Requires:      /bin/bash, iputils-ping
BuildArch:     noarch

%description
Этот скрипт проверяет доступность хостов в сети с помощью команды ping.

%prep
%setup -q

%install
mkdir -p %{buildroot}%{_bindir}
install -m 755 check_hosts.sh %{buildroot}%{_bindir}

%files
%{_bindir}/check_hosts.sh

%changelog
* Fri Sep 27 2024 Valeria Lee <lerakenobi@example.com> 1.0-1
- Initial package for check_hosts
