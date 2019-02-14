package be.solodoukhin.service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.02.13
 */
@Slf4j
@Service
public class AuthenticationService {

    @PersistenceContext
    private EntityManager em;

    private static final byte DATABASE_CHARACTERS = 2;
    private static final String APPLICATION_NAME = "OBSERVATOIRE";

    private static final String FIND_USERNAME_BY_TOKEN_QUERY =
            "SELECT " +
            "u.NOM_UTILISATEUR " +
            "FROM HR_TOKEN t " +
            "INNER JOIN HR_UTILISATEUR u ON t.IT_UTILISATEUR = u.ID_TECHNIQUE " +
            "WHERE t.token = :token " +
            "AND (u.DATE_SUPPRESSION_LOGIQUE is null or u.DATE_SUPPRESSION_LOGIQUE > :cur_date)";

    private static final String USER_APPLICATION_ACCESS_QUERY =
            "SELECT " +
            "count(*) " +
            "FROM hr_utilisateur u " +
            "INNER JOIN hr_jouer j ON u.id_technique = j.JOUEUR " +
            "INNER JOIN hr_role r ON j.it_role = r.id_technique " +
            "INNER JOIN hr_acceder a ON r.id_technique = a.IT_ROLE " +
            "INNER JOIN hr_s_ressource res ON res.ID_TECHNIQUE = a.it_ressource " +
            "INNER JOIN fonction f ON res.ressource = f.id_technique " +
            "WHERE " +
            "    u.nom_utilisateur = :username  " +
            "    AND f.id_application = :application " +
            "    AND (u.DATE_SUPPRESSION_LOGIQUE IS NULL OR u.DATE_SUPPRESSION_LOGIQUE > :cur_date) " +
            "    AND (j.DATE_SUPPRESSION_LOGIQUE IS NULL OR j.DATE_SUPPRESSION_LOGIQUE > :cur_date) " +
            "    AND (r.DATE_SUPPRESSION_LOGIQUE IS NULL OR r.DATE_SUPPRESSION_LOGIQUE > :cur_date) " +
            "    AND (a.DATE_SUPPRESSION_LOGIQUE IS NULL OR a.DATE_SUPPRESSION_LOGIQUE > :cur_date) " +
            "    AND (res.DATE_SUPPRESSION_LOGIQUE IS NULL OR res.DATE_SUPPRESSION_LOGIQUE > :cur_date) " +
            "    AND :cur_date BETWEEN j.per_dt_debut AND j.per_dt_fin " +
            "    AND :cur_date BETWEEN r.per_dt_debut AND r.per_dt_fin ";

    public Optional<String> getCurrentUserName(String token, LocalDate validUntilDate) {
        token = token.substring(0, token.length() - DATABASE_CHARACTERS);
        String username = null;
        try{
            username = (String) em.createNativeQuery(FIND_USERNAME_BY_TOKEN_QUERY)
                    .setParameter("token", token)
                    .setParameter("cur_date", validUntilDate)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.warn("Could not find username by token.");
        }

        return Optional.ofNullable(username);
    }

    public boolean hasAccess(String username, LocalDate validUntilDate) {
        return this.hasAccess(username, validUntilDate, APPLICATION_NAME);
    }

    boolean hasAccess(String username, LocalDate validUntilDate, String applicationName) {
        BigDecimal size = null;
        try{
            size = (BigDecimal) this.em.createNativeQuery(USER_APPLICATION_ACCESS_QUERY)
                    .setParameter("username", username)
                    .setParameter("application", applicationName)
                    .setParameter("cur_date", validUntilDate)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.warn("An unexpected error occurred on a count query", e);
        }

        return size != null && !size.equals(BigDecimal.ZERO);
    }
}
