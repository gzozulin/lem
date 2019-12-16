<#if newWindow>
    <a class="${class}" href="${link}" target="_blank">${label?no_esc}</a>
<#else>
    <a class="${class}" href="${link}">${label?no_esc}</a>
</#if>