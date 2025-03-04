//Lista de usuarios
document.addEventListener('DOMContentLoaded', function () {
    loadUsers();
});

function loadUsers() {
    fetch('/api/users')
        .then(response => response.json())
        .then(users => {
            const userTableBody = document.getElementById('userTableBody');
            userTableBody.innerHTML = ''; // Limpiar la tabla

            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.email}</td>
                    <td>${user.firstName} ${user.lastName}</td>
                    <td>${user.address}</td>
                    <td>${user.phone}</td>
                    <td>${user.role}</td>
                    <td>${user.enabled ? 'Habilitado' : 'Deshabilitado'}</td>
                    <td>
                        <button class="btn btn-sm btn-warning" onclick="editUser(${user.id})">Editar</button>
                        <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.id})">Eliminar</button>
                        <button class="btn btn-sm btn-info" onclick="toggleUserStatus(${user.id}, ${!user.enabled})">
                            ${user.enabled ? 'Deshabilitar' : 'Habilitar'}
                        </button>
                        <button class="btn btn-sm btn-secondary" onclick="changeUserRole(${user.id}, '${user.role === 'USER' ? 'ADMIN' : 'USER'}')">
                            Cambiar a ${user.role === 'ROLE_USER' ? 'ROLE_ADMIN' : 'USER'}
                        </button>
                    </td>
                `;
                userTableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error al cargar usuarios:', error));
}

//Perfil admin
document.getElementById('updateProfileForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const updatedProfile = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        address: document.getElementById('address').value,
        phone: document.getElementById('phone').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        confirmPassword: document.getElementById('confirmPassword').value
    };

    if (updatedProfile.password !== updatedProfile.confirmPassword) {
        alert('Las contraseñas no coinciden.');
        return;
    }

    fetch('/api/users/current', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedProfile)
    })
    .then(response => {
        if (response.ok) {
            alert('Perfil actualizado correctamente.');
            loadUsers(); // Recargar la lista de usuarios
        } else {
            alert('Error al actualizar el perfil.');
        }
    })
    .catch(error => console.error('Error:', error));
});

//Cambiar role
function changeUserRole(userId, newRole) {
    const token = localStorage.getItem('token'); // Otener el token
    if (!token) {
        alert("No tienes permisos.");
        return;
    }

    fetch(`/api/users/${userId}/role`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` // Envía el token
        },
        body: JSON.stringify({ role: newRole })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }
        return response.text();
    })
    .then(message => {
        alert(message);
        loadUsers();
    })
    .catch(error => console.error('Error:', error));
}


//Habilitar usuario
function toggleUserStatus(userId, newStatus) {
    const token = localStorage.getItem('token');
    if (!token) {
        alert("No tienes permisos.");
        return;
    }

    const url = newStatus ? `/api/users/${userId}/enable` : `/api/users/${userId}/disable`;

    console.log("Enviando token:", token); // Verifica si el token es válido

    fetch(url, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        credentials: 'include'
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.text();
    })
    .then(message => {
        alert(message);
        loadUsers();
    })
    .catch(error => {
        console.error('Error:', error);
        alert("Error al actualizar el estado del usuario: " + error.message);
    });
}



//Eliminar usuario
function deleteUser(userId) {
    if (confirm('¿Estás seguro de eliminar este usuario?')) {
        fetch(`/api/users/${userId}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Usuario eliminado correctamente.');
                loadUsers(); // Recargar la lista de usuarios
            } else {
                alert('Error al eliminar el usuario.');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

//Editar usuario
function editUser(userId) {
    fetch(`/api/users/${userId}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('firstName').value = user.firstName;
            document.getElementById('lastName').value = user.lastName;
            document.getElementById('address').value = user.address;
            document.getElementById('phone').value = user.phone;
            document.getElementById('email').value = user.email;
        })
        .catch(error => console.error('Error:', error));
}
