import type { ReactNode } from "react";
import { useKeycloak } from "@react-keycloak/web";
import { Navigate } from "react-router-dom";

interface PrivateRouteProps {
  children: ReactNode;
  roles?: string[];
}

export default function PrivateRoute({ children, roles = [] }: PrivateRouteProps) {
  const { keycloak, initialized } = useKeycloak();

  if (!initialized) return <div>Loading...</div>;

  if (!keycloak?.authenticated) {
    keycloak.login();
    return null;
  }

  if (roles.length > 0) {
    const realmRoles: string[] = keycloak?.tokenParsed?.realm_access?.roles || [];
    const hasRole = roles.some((r) => realmRoles.includes(r));
    if (!hasRole) {
      return <Navigate to="/forbidden" replace />;
    }
  }

  return <>{children}</>;
}
