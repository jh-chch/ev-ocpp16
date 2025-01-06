// Constants
const CONFIG = {
    REFRESH_INTERVAL: 60000,
    PAGE_SIZE: 5,
    PAGINATION_GROUP_SIZE: 5
};

const ENDPOINTS = {
    CHARGERS: '/chargers',
    MEMBERS: '/members',
    SUMMARY_EXCEL: '/members/charge-history/excel',
    DETAILED_EXCEL: (idToken) => `/members/${idToken}/charge-history/excel`
};

const DOM = {
    init() {
        this.siteSelect = document.getElementById('siteNames');
        this.chargersDiv = document.getElementById('chargers');
        this.membersDiv = document.getElementById('members');
        this.searchType = document.getElementById('memberSearchType');
        this.searchValue = document.getElementById('memberSearchValue');
        this.startDatetime = document.getElementById('startDatetime');
        this.endDatetime = document.getElementById('endDatetime');
        this.downloadSummaryButton = document.getElementById('downloadSummaryButton');
    }
};

const DataService = {
    async fetchData(url, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const response = await fetch(`${url}?${queryString}`);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    },

    async downloadExcel(url, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const response = await fetch(`${url}?${queryString}`);
        if (!response.ok) throw new Error('다운로드 실패');
        return response.blob();
    }
};

// Charger Service
const ChargerService = {
    async fetchChargers() {
        const siteName = DOM.siteSelect.value;
        if (!siteName) {
            return UIService.renderMessage(DOM.chargersDiv, '사이트가 존재하지 않습니다.');
        }

        try {
            const data = await DataService.fetchData(ENDPOINTS.CHARGERS, { siteName });
            if (!data.chargers?.length) {
                return UIService.renderMessage(DOM.chargersDiv, '충전기가 존재하지 않습니다.');
            }
            UIService.renderChargers(data.chargers);
        } catch (error) {
            console.error('충전기 목록 조회 오류:', error);
            UIService.renderMessage(DOM.chargersDiv, '충전기 목록을 불러오는 중 오류가 발생했습니다.');
        }
    }
};

// Member Service
const MemberService = {
    async fetchMembers(page = 0) {
        const siteName = DOM.siteSelect.value;
        if (!siteName) {
            return UIService.renderMessage(DOM.membersDiv, '사이트가 존재하지 않습니다.');
        }

        try {
            const params = {
                siteName,
                searchType: DOM.searchType.value,
                searchValue: DOM.searchValue.value,
                page,
                size: CONFIG.PAGE_SIZE
            };

            const data = await DataService.fetchData(ENDPOINTS.MEMBERS, params);
            if (!data.members?.length) {
                return UIService.renderMessage(DOM.membersDiv, '회원이 없습니다.');
            }
            UIService.renderMembers(data);
        } catch (error) {
            console.error('회원 목록 조회 오류:', error);
            UIService.renderMessage(DOM.membersDiv, '오류가 발생했습니다. 다시 시도해주세요.');
        }
    }
};

// Excel Service
const ExcelService = {
    async validateDateRange() {
        const startDatetime = DOM.startDatetime.value;
        const endDatetime = DOM.endDatetime.value;

        if (!startDatetime || !endDatetime) {
            alert('조회 기간을 선택해주세요.');
            return false;
        }
        return { startDatetime, endDatetime };
    },

    async downloadExcel(url, filename, params) {
        try {
            const blob = await DataService.downloadExcel(url, params);
            UIService.triggerDownload(blob, filename);
        } catch (error) {
            console.error('Excel 다운로드 오류:', error);
            alert('Excel 다운로드 중 오류가 발생했습니다.');
        }
    },

    async downloadSummaryChargeHistory() {
        const dateRange = await this.validateDateRange();
        if (!dateRange) return;

        const filename = `충전이력_전체_${dateRange.startDatetime}_${dateRange.endDatetime}.xlsx`;
        await this.downloadExcel(ENDPOINTS.SUMMARY_EXCEL, filename, dateRange);
    },

    async downloadDetailedChargeHistory(idToken) {
        const dateRange = await this.validateDateRange();
        if (!dateRange) return;

        const filename = `충전이력_${idToken}_${dateRange.startDatetime}_${dateRange.endDatetime}.xlsx`;
        await this.downloadExcel(ENDPOINTS.DETAILED_EXCEL(idToken), filename, dateRange);
    }
};

// UI Service
const UIService = {
    renderMessage(element, message) {
        element.innerHTML = `<p class="message">${message}</p>`;
    },

    renderChargers(chargers) {
        const chargersList = document.createElement('ul');
        chargersList.className = 'charger-list';
        
        chargersList.innerHTML = chargers.map(({ name, serialNumber, connectionStatus }) => `
            <li class="charger-item status-${connectionStatus}">
                <div class="charger-name">${name}</div>
                <div class="charger-serial">${serialNumber}</div>
            </li>
        `).join('');
        
        DOM.chargersDiv.innerHTML = '';
        DOM.chargersDiv.appendChild(chargersList);
    },

    renderMembers(data) {
        const tableHtml = this.createMemberTable(data.members);
        const paginationHtml = this.createMemberPagination(data.currentPage, data.totalPages);
        DOM.membersDiv.innerHTML = tableHtml + paginationHtml;
    },

    createMemberTable(members) {
        return `
            <table>
                <thead>${this.createMemberTableHeader()}</thead>
                <tbody>${members.map(member =>  this.createMemberTableRow(member)).join('')}</tbody>
            </table>
        `;
    },

    createMemberTableHeader() {
        return `
            <tr>
                <th>이메일</th>
                <th>이름</th>
                <th>전화번호</th>
                <th>차량번호</th>
                <th>주소</th>
                <th>권한</th>
                <th>계정상태</th>
                <th>충전이력</th>
            </tr>
        `;
    },

    createMemberTableRow(member) {
        return `
            <tr>
                <td>${member.email}</td>
                <td>${member.username}</td>
                <td>${member.phoneNumber}</td>
                <td>${member.carNumber}</td>
                <td>${this.formatAddress(member.address)}</td>
                <td>${member.role === 'ROLE_ADMIN' ? '관리자' : '사용자'}</td>
                <td>${member.accountStatus}</td>
                <td>
                    <button onclick="ExcelService.downloadDetailedChargeHistory('${member.idToken}')">Excel</button>
                </td>
            </tr>
        `;
    },

    formatAddress(address) {
        return [address.zipCode, address?.address1, address?.address2]
            .filter(Boolean)
            .join(' ');
    },

    createMemberPagination(currentPage, totalPages) {
        const pageGroup = Math.ceil((currentPage + 1) / CONFIG.PAGINATION_GROUP_SIZE);
        const lastPageGroup = Math.ceil(totalPages / CONFIG.PAGINATION_GROUP_SIZE);
        const startPage = (pageGroup - 1) * CONFIG.PAGINATION_GROUP_SIZE + 1;
        const endPage = Math.min(startPage + CONFIG.PAGINATION_GROUP_SIZE - 1, totalPages);

        return `
            <div class="pagination">
                ${this.createMemberPaginationButtons(pageGroup, lastPageGroup, startPage, endPage, currentPage + 1)}
            </div>
        `;
    },

    createMemberPaginationButtons(pageGroup, lastPageGroup, startPage, endPage, currentPage) {
        const prevButton = pageGroup > 1 
            ? `<button onclick="MemberService.fetchMembers(${startPage - 2})">이전</button>` 
            : '';

        const pageButtons = Array.from(
            { length: endPage - startPage + 1 }, 
            (_, i) => startPage + i
        ).map(pageNum => `
            <button 
                onclick="MemberService.fetchMembers(${pageNum - 1})"
                class="${pageNum === currentPage ? 'active' : ''}"
            >${pageNum}</button>
        `).join('');

        const nextButton = pageGroup < lastPageGroup 
            ? `<button onclick="MemberService.fetchMembers(${endPage})">다음</button>` 
            : '';

        return prevButton + pageButtons + nextButton;
    },

    triggerDownload(blob, filename) {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        a.remove();
    }
};

const AutoRefreshService = {
    refreshInterval: null,

    start(callback, interval = CONFIG.REFRESH_INTERVAL) {
        this.stop();
        this.refreshInterval = setInterval(() => {
            callback().catch(console.error);
        }, interval);
    },

    stop() {
        if (this.refreshInterval) {
            clearInterval(this.refreshInterval);
        }
    }
};

const App = {
    init() {
        DOM.init();
        this.attachEventListeners();
        this.refreshData();
    },

    attachEventListeners() {
        DOM.siteSelect.addEventListener('change', () => this.refreshData());
        DOM.searchValue.addEventListener('input', () => MemberService.fetchMembers(0));
        DOM.searchType.addEventListener('change', () => MemberService.fetchMembers(0));
        DOM.downloadSummaryButton.addEventListener('click', () => ExcelService.downloadSummaryChargeHistory());
    },

    refreshData() {
        ChargerService.fetchChargers();
        MemberService.fetchMembers();
        AutoRefreshService.start(ChargerService.fetchChargers);
    }
};

document.addEventListener('DOMContentLoaded', () => App.init()); 