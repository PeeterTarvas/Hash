SELECT aine, nimetus
FROM aine
WHERE aine_kood IN (
    SELECT aine
    FROM oppimine
    WHERE oppimise_algus >= '2000-09-01':: date
    )

INSERT INTO aktiivne_oppeaine(aine_kood, nimetus) (
    SELECT DISTINCT aine
    FROM oppimine INNER JOIN aine ON oppimine.aine = aine.aine
    WHERE oppimise_algus >= '2000-09-01':: date
    )



WITH as im(
    SELECT tudeng
    FROM oppimine LEFT JOIN eksam ON oppimine.oppimine = aine.oppimine
    WHERE eksam.oppimine IS NULL
    ), sm (SELECT tudeng
    FROM eksam, oppimine
    WHERE tulemus = 0
    AND eksam.oppimine = oppimine.oppimine
    GROUP BY tudeng
    HAVING COUNT(*) > 2;)
DELETE FROM tudeng
WHERE tudkood IN im AND NOT IN sm


SELECT COUNT(*) * 100 / ROUND(SELECT iff(COUNT(*)=0, NULL,  COUNT(*)) AS arv
    FROM kylaline, 1) AS protsent
FROM kylaline
WHERE NOT EXSISTS (
    SELECT *
    FROM reserveerimine
    WHERE kylalise.nr = reserveerimine.nr
    );



DROP table reserveerimine_koopia;
SELECT * INTO reserveerimise_koopia
FROM reserveerimine;

DELETE FROM reserveerimise_koopia
WHERE EXTRACT(YEAR FROM alguse_aeg) <> 2015 AND EXTRACT(YEAR FROM alguse_aeg) <> 2012
   AND (lopu_aeg::date - alguse_aeg::date) > (
    SELECT (AVG(lopu_aeg::date - alguse_aeg::date) + 1) AS keskmine
    FROM reserveerimise_koopia
    );

UPDATE ruum
SET hind = hind * 0.95
WHERE (hotelli_nr, ruumi_nr) NOT IN (
    SELECT hotelli_nr, reserveerimine.ruumi_nr
    FROM reserveerimine
    GROUP BY reserveerimine.ruumi_nr
    HAVING COUNT(*) < 2
    UNION SELECT hotelli_nr, ruumi_nr
    FROM ruum
    WHERE (hotelli_nr, ruumi_nr) NOT IN (
        SELECT reserveerimine.hotelli_nr,reserveerimine.ruumi_nr
        FROM reserveerimine
    )
);


INSERT INTO kylaline_varu (kylalise_nr, nimi)
SELECT kylalise_nr, CONCAT(eesnimi, ' ', perenimi)
FROM kylaline
WHERE kylalise_nr IN (
        SELECT kylalise_nr
        FROM (
                 SELECT kylalise_nr, hotelli_nr
                 FROM reserveerimine
                 WHERE hotelli_nr IN (
                     SELECT hotelli_nr
                     FROM hotell
                     WHERE linn = 'Tallinn'
                 )
                 GROUP BY kylalise_nr, hotelli_nr
             ) as alam
        GROUP BY kylalise_nr
        HAVING COUNT(alam.kylalise_nr) = 1
        )


SELECT ruumi_nr
FROM ruum
WHERE ruumi_tyyp = 'Luksusnumber'



UPDATE ruum
SET hind = hind * 0.95
WHERE (hotelli_nr, ruumi_nr) IN (
    SELECT hotelli_nr, reserveerimine.ruumi_nr
    FROM reserveerimine
    GROUP BY hotelli_nr, reserveerimine.ruumi_nr
    HAVING COUNT(*) < 2
    UNION SELECT hotelli_nr, ruumi_nr
    FROM ruum
    WHERE (hotelli_nr, ruumi_nr) NOT IN (
        SELECT reserveerimine.hotelli_nr,reserveerimine.ruumi_nr
        FROM reserveerimine
        GROUP BY reserveerimine.hotelli_nr, reserveerimine.ruumi_nr
    )

) AND ruum.hotelli_nr IN (SELECT hotelli_nr FROM hotell WHERE linn = 'Tallinn') ;

SELECT hotelli_nr, nimi, protsent
FROM (
         WITH tais AS (
             SELECT hotelli_nr, COUNT(*) AS tais_arv
             FROM reserveerimine
             GROUP BY hotelli_nr
         ), part AS (
              SELECT hotelli_nr, COUNT(*) AS part_arv
              FROM reserveerimine
              WHERE lopu_aeg - alguse_aeg > 5
              GROUP BY hotelli_nr
              )
         SELECT tais.hotelli_nr AS hotell_nr, part_arv, tais_arv,
                NULLIF(ROUND((part_arv::decimal / tais_arv::decimal) * 100, 1), 0) AS protsent
         FROM tais, part
         WHERE tais.hotelli_nr = part.hotelli_nr
) as htl INNER JOIN hotell ON hotell.hotelli_nr = htl.hotell_nr;



SELECT hotelli_nr
FROM (
         SELECT hotelli_nr, COUNT(*)
         FROM reserveerimine
         group by hotelli_nr
     )


SELECT * INTO kylaline_koopia
FROM kylaline
WHERE FALSE;

DELETE FROM kylaline_koopia;
ALTER TABLE kylaline_koopia ADD CONSTRAINT pk_key_kylalise_nr PRIMARY KEY (kylalise_nr);

INSERT INTO kylaline_koopia(kylalise_nr, eesnimi, perenimi, aadress)
         SELECT kylalise_nr, eesnimi, perenimi, aadress
         FROM kylaline
         WHERE kylalise_nr NOT IN(
             SELECT kylalise_nr
             FROM reserveerimine
             WHERE (hotelli_nr, ruumi_nr) IN (
                 SELECT hotelli_nr, ruumi_nr
                 FROM ruum
                 WHERE ruumi_tyyp = 'Luksusnumber'
                   AND ruum.hotelli_nr IN (
                     SELECT hotelli_nr
                     FROM hotell
                     WHERE linn = 'Tallinn'
                 )
             )
         );