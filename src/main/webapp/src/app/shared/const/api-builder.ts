const appBaseHref: string = getAppBaseHref();

export function apiBuilder(obj: any, prefix: string, baseHref?: string) {
  prefix += obj.url || '';
  Object.keys(obj).forEach((k) => {
    if (typeof obj[k] === 'object') {
      apiBuilder(obj[k], prefix, baseHref);
    } else {
      const baseUrl = baseHref ?? appBaseHref;

      obj[k] = baseUrl + prefix + (k !== 'url' ? obj[k] : '');
    }
  });

  return obj;
}

function getAppBaseHref(): string {
  const baseUrl = new URL(document.baseURI);
  const baseHref = baseUrl.pathname;
  return baseHref.endsWith('/') ? baseHref.substring(0, baseHref.length - 1) : baseHref;
}
