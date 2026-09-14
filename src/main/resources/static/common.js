// common.js — shared helpers used across pages (auth state, toast, nav)

function money(n) { return '₦' + n.toLocaleString(); }

function getToken() { return localStorage.getItem('qc_token'); }
function getUser() {
  const raw = localStorage.getItem('qc_user');
  return raw ? JSON.parse(raw) : null;
}
function setSession(token, user) {
  localStorage.setItem('qc_token', token);
  localStorage.setItem('qc_user', JSON.stringify(user));
}
function clearSession() {
  localStorage.removeItem('qc_token');
  localStorage.removeItem('qc_user');
}

function showToast(msg, isError) {
  let t = document.getElementById('toast');
  if (!t) {
    t = document.createElement('div');
    t.id = 'toast';
    t.className = 'toast';
    document.body.appendChild(t);
  }
  t.textContent = msg;
  t.className = 'toast show' + (isError ? ' error' : '');
  setTimeout(() => { t.className = 'toast' + (isError ? ' error' : ''); }, 2200);
}

// Renders the account area of the nav (Sign in vs. Hi, Name)
function renderAccountNav(elId) {
  const el = document.getElementById(elId);
  if (!el) return;
  const user = getUser();
  if (user) {
    el.innerHTML = `
      <a href="orders.html">My orders</a>
      <button class="account-pill" onclick="logout()">Hi, ${user.name.split(' ')[0]} — Log out</button>
    `;
  } else {
    el.innerHTML = `<a href="login.html" class="account-pill">Sign in</a>`;
  }
}

function logout() {
  clearSession();
  showToast('Logged out');
  setTimeout(() => window.location.href = 'index.html', 600);
}

// Wrapper around fetch that attaches the auth token automatically
async function api(path, options = {}) {
  const token = getToken();
  const headers = Object.assign({ 'Content-Type': 'application/json' }, options.headers || {});
  if (token) headers['Authorization'] = 'Bearer ' + token;

  const res = await fetch(path, Object.assign({}, options, { headers }));
  const data = await res.json().catch(() => ({}));
  if (!res.ok) {
    throw new Error(data.error || 'Something went wrong. Please try again.');
  }
  return data;
}
