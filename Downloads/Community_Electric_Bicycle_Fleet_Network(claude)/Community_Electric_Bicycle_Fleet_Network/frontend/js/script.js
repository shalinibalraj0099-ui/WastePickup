/* ==========================================================================
   Community Shared Electric Bicycle & Micromobility Fleet Network
   Frontend application script — talks to the REST API under /api/**
   ========================================================================== */

/* ---------------------------- tiny API helpers ---------------------------- */
const API = {
  async get(url) {
    const r = await fetch(url);
    if (!r.ok) throw new Error(await safeErr(r));
    return r.status === 204 ? null : r.json();
  },
  async post(url, body) {
    const r = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
    if (!r.ok) throw new Error(await safeErr(r));
    return r.status === 204 ? null : r.json();
  },
  async put(url, body) {
    const r = await fetch(url, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
    if (!r.ok) throw new Error(await safeErr(r));
    return r.status === 204 ? null : r.json();
  },
  async del(url) {
    const r = await fetch(url, { method: 'DELETE' });
    if (!r.ok) throw new Error(await safeErr(r));
    return true;
  }
};
async function safeErr(r) {
  try { const j = await r.json(); return j.error || (r.status + ' ' + r.statusText); }
  catch (e) { return r.status + ' ' + r.statusText; }
}

/* ------------------------------- toast ------------------------------- */
function toast(msg, isError) {
  let root = document.getElementById('toastRoot');
  if (!root) {
    root = document.createElement('div');
    root.id = 'toastRoot';
    document.body.appendChild(root);
  }
  const t = document.createElement('div');
  t.className = 'toast' + (isError ? ' error' : '');
  t.textContent = msg;
  root.appendChild(t);
  setTimeout(() => t.remove(), 3500);
}

/* --------------------------- sidebar (mobile) --------------------------- */
function toggleSidebar() {
  document.getElementById('sidebar')?.classList.toggle('open');
  document.getElementById('sidebarOverlay')?.classList.toggle('show');
}
function closeSidebar() {
  document.getElementById('sidebar')?.classList.remove('open');
  document.getElementById('sidebarOverlay')?.classList.remove('show');
}

/* --------------------------------- badges -------------------------------- */
function statusBadge(status) {
  if (!status) return '<span class="badge badge-gray">-</span>';
  const s = String(status).toUpperCase();
  let cls = 'badge-gray';
  if (['ACTIVE', 'COMPLETED', 'AVAILABLE', 'PAID', 'APPROVED', 'DELIVERED', 'CONFIRMED'].includes(s)) cls = 'badge-green';
  else if (['ONGOING', 'PENDING', 'IN_RIDE', 'CREATED', 'CHARGING', 'UNPAID'].includes(s)) cls = 'badge-amber';
  else if (['LOW_BATTERY', 'CANCELLED', 'OVERDUE', 'FAILED', 'REJECTED'].includes(s)) cls = 'badge-red';
  else cls = 'badge-blue';
  return `<span class="badge ${cls}">${status}</span>`;
}
function money(v) {
  const n = Number(v || 0);
  return '₹' + n.toLocaleString('en-IN', { maximumFractionDigits: 2 });
}
function fmtDate(v) {
  if (!v) return '-';
  try { return new Date(v).toLocaleString('en-IN', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' }); }
  catch (e) { return v; }
}

/* ============================================================================
   Generic CRUD table + modal engine.
   Every simple module page (bikes, stations, contacts sub-tabs, products,
   purchase orders, vendor bills, sales orders, invoices, payments, accounts,
   journals, ledger, budgets ...) calls initCrudPage() with a small config
   instead of hand-writing bespoke fetch/render code for every module.
   ============================================================================ */
function ensureModalRoot() {
  if (document.getElementById('modalRoot')) return;
  const div = document.createElement('div');
  div.id = 'modalRoot';
  document.body.appendChild(div);
}

function initCrudPage(cfg) {
  ensureModalRoot();
  const state = { rows: [], editingId: null };
  const tbody = document.getElementById(cfg.tableBodyId);
  const countEl = cfg.countId ? document.getElementById(cfg.countId) : null;

  function fieldInputHtml(f, value) {
    const val = value === undefined || value === null ? '' : value;
    if (f.type === 'select') {
      const opts = (f.options || []).map(o => `<option value="${o.value}" ${String(o.value) === String(val) ? 'selected' : ''}>${o.label}</option>`).join('');
      return `<select id="f_${f.key}" ${f.required ? 'required' : ''}>${opts}</select>`;
    }
    if (f.type === 'textarea') {
      return `<textarea id="f_${f.key}" rows="3" ${f.required ? 'required' : ''}>${val}</textarea>`;
    }
    return `<input type="${f.type || 'text'}" id="f_${f.key}" value="${val}" ${f.step ? `step="${f.step}"` : ''} ${f.required ? 'required' : ''} placeholder="${f.placeholder || ''}">`;
  }

  function openModal(row) {
    state.editingId = row ? row[cfg.idField || 'id'] : null;
    const isEdit = !!state.editingId;
    const fieldsHtml = cfg.fields.map(f => `
      <div class="form-row">
        <label>${f.label}${f.required ? ' *' : ''}</label>
        ${fieldInputHtml(f, row ? row[f.key] : (f.default ?? ''))}
      </div>`).join('');

    document.getElementById('modalRoot').innerHTML = `
      <div class="modal-backdrop show" id="crudModalBackdrop">
        <div class="modal-box">
          <div class="modal-head">
            <h3>${isEdit ? 'Edit' : 'Add'} ${cfg.title}</h3>
            <button class="icon-btn" onclick="document.getElementById('crudModalBackdrop').remove()">✕</button>
          </div>
          <form id="crudForm">${fieldsHtml}
            <div class="modal-foot">
              <button type="button" class="btn btn-outline" onclick="document.getElementById('crudModalBackdrop').remove()">Cancel</button>
              <button type="submit" class="btn btn-primary">${isEdit ? 'Save Changes' : 'Add ' + cfg.title}</button>
            </div>
          </form>
        </div>
      </div>`;

    document.getElementById('crudForm').addEventListener('submit', async (e) => {
      e.preventDefault();
      const payload = {};
      cfg.fields.forEach(f => {
        const el = document.getElementById('f_' + f.key);
        let v = el.value;
        if (f.type === 'number') v = v === '' ? null : Number(v);
        payload[f.key] = v;
      });
      try {
        if (isEdit) {
          await API.put(`${cfg.endpoint}/${state.editingId}`, payload);
          toast(cfg.title + ' updated');
        } else {
          await API.post(cfg.endpoint, payload);
          toast(cfg.title + ' added');
        }
        document.getElementById('crudModalBackdrop').remove();
        load();
      } catch (err) {
        toast(err.message, true);
      }
    });
  }

  async function removeRow(id) {
    if (!confirm(`Delete this ${cfg.title.toLowerCase()}?`)) return;
    try {
      await API.del(`${cfg.endpoint}/${id}`);
      toast(cfg.title + ' deleted');
      load();
    } catch (err) { toast(err.message, true); }
  }

  function render() {
    if (!tbody) return;
    if (!state.rows.length) {
      tbody.innerHTML = `<tr class="empty-row"><td colspan="${cfg.columns.length + 1}">No records yet. Click "Add ${cfg.title}" to create one.</td></tr>`;
      return;
    }
    tbody.innerHTML = state.rows.map(row => {
      const cells = cfg.columns.map(c => `<td>${c.render ? c.render(row) : (row[c.key] ?? '-')}</td>`).join('');
      const id = row[cfg.idField || 'id'];
      return `<tr>
        ${cells}
        <td>
          <div class="row-actions">
            <button class="icon-btn" title="Edit" onclick='window.__crud_${cfg.key}.edit(${id})'>✎</button>
            <button class="icon-btn" title="Delete" onclick='window.__crud_${cfg.key}.remove(${id})'>🗑</button>
          </div>
        </td>
      </tr>`;
    }).join('');
  }

  async function load() {
    try {
      state.rows = await API.get(cfg.endpoint) || [];
      if (countEl) countEl.textContent = state.rows.length;
      render();
      if (cfg.onLoaded) cfg.onLoaded(state.rows);
    } catch (err) {
      if (tbody) tbody.innerHTML = `<tr class="empty-row"><td colspan="${cfg.columns.length + 1}">Could not load data (${err.message}). Is the backend / MySQL running?</td></tr>`;
    }
  }

  // Expose a small handle so inline onclick="" attributes can reach this instance
  window['__crud_' + cfg.key] = {
    edit(id) { const row = state.rows.find(r => String(r[cfg.idField || 'id']) === String(id)); openModal(row); },
    remove(id) { removeRow(id); }
  };

  const addBtn = document.getElementById(cfg.addButtonId);
  if (addBtn) addBtn.addEventListener('click', () => openModal(null));

  load();
  return { reload: load };
}

/* --------------------------- dashboard summary --------------------------- */
async function loadDashboardSummary() {
  const els = {
    bikes: document.getElementById('kpiBikes'),
    rides: document.getElementById('kpiRides'),
    stations: document.getElementById('kpiStations'),
    revenue: document.getElementById('kpiRevenue'),
    healthy: document.getElementById('battHealthy'),
    low: document.getElementById('battLow'),
    charging: document.getElementById('battCharging'),
  };
  try {
    const d = await API.get('/api/reports/dashboard');
    if (els.bikes) els.bikes.textContent = d.totalBikes;
    if (els.rides) els.rides.textContent = d.activeRides;
    if (els.stations) els.stations.textContent = d.totalStations;
    if (els.revenue) els.revenue.textContent = money(d.totalRevenue);
    if (els.healthy) els.healthy.textContent = d.batteryHealthy;
    if (els.low) els.low.textContent = d.batteryLow;
    if (els.charging) els.charging.textContent = d.batteryCharging;
  } catch (e) { console.log('dashboard summary not available yet', e); }
}

document.addEventListener('DOMContentLoaded', () => {
  if (document.getElementById('kpiBikes')) loadDashboardSummary();
});
