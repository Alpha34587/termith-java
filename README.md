# Termith-Java

Termith-java est une chaîne de traitement qui extrait et désambiguise la terminologie de corpus au format tei/xml. A la fin du traitements les fichiers du corpus sont enrichis par différents types d'annotations au format standOff.
Cette application est développée dans le cadre du projet ISTEX, elle doit être assez performante pour traiter des corpus volumineux (+ de 10000 fichiers).

## Le module d'extraction terminologique

1. analyse morphosyntaxique et tokenisation à l'aide de treetagger ou de taslimane (l'implémentation de talismane est en cours)
2. extraction terminologique à l'aide de [termsuite](http://termsuite.github.io/)
3. le résultat de cette procédure produits 3 types d'enrichissements :
  - une tokenisation sur l'éléments text
  - l'ajout d'un élément standOff (l'attribut type a pour valeur 'wordForms') qui contient l'ensemble l'analyse morphosyntaxique
  - l'ajout d'un élément standOff (l'attribut type a pour valeur candidatsTermes) qui contient l'ensemble de l'extraction terminologique
4. une terminologie au format json et tbx

## Le module de désambiguisation

1. le module de désambiguisation prend pour entrée des fichiers enrichis par la procédure précedente.
2. il est néccéssaire d'avoir un corpus d'apprentissage qui a été au préalable annoté manuellement.
3. il est néccéssaire d'avoir un corpus d'évaluation.
4. le résultats de cette procédure enrichis les candidat termes de l'éléments standOff (qui a pour type 'candidatsTermes') contenant l'ensemble des candidats termes extraits. l'attribut ana des candidats termes pourra prendre 3 valeurs différentes en fonction de la décisions prises par la procédure de désambiguisation :
  - DaOn si le candidat terme est considéré comme terminologique
  - DaOff si le candidat terme est considéré comme non-terminologique
  - noDa si la procédure n'a pris aucune décision

## Applications externes requises

- [R](https://www.r-project.org/)
- [TreeTagger](http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/)

## Installation

1. s'assurer que TreeTagger, R et java 8 soient bien installés
2. télécharger la dernière version de termith-java disponible [ici]( https://github.com/simonmeoni/termith-java/releases)
3. installer les librairie R suivantes : `data.table` & `Rserve`
4. pour installer `Rserve` :
  1. ouvrir un terminal et lancer la commande `R`
  2. dans l'interpréteur de commande R, taper la commande : `install.packages("Rserve")` et suivre les instructions
  3. taper ensuite la commmande `library('Rserve')` pour charger la librairie précédemment installé
  4. taper ensuite `Rserve()` pour lancer le serveur R. un sous-processus est lancé en tâche de fond, le terminal peut être fermé.
5. pour installer `data.table`, il faut lancer une instance de R (comme ci-dessus) et lancer la commande `install.packages('data.table')`

## Comment utiliser Termith-Java ?

1. l'analyse morphosyntaxique et terminologique est appélé avec la commande suivante :
`termITH-all-v0.4-SNAPSHOT.jar org.atilf.cli.TermithTreeTaggerCLI`
`-i /chemin/vers/corpus/tei`
`-o /chemin/vers/sortie`
`-l en`
`-tt chemin/vers/dossier/installation/de/treetagger`
  * l'option `-i` correspond au corpus d'entrée donné au programme
  * l'option `-o` correspond au chemin où seront écrits les résultats
  * l'option `-l` à la langue du corpus d'entrée
  * l'option `-tt` au chemin du **dossier d'installation** de treetagger  

2. La désambiguisation est appelé avec la commande suivante :
`org.atilf.cli.DisambiguationCLI`
`-le /home/smeoni/Documents/desamb/res/learning-test`
`-e /home/smeoni/Documents/desamb/res/evaluation`
`-o /home/smeoni/Documents/test/archeo-desamb`
  * l'option `-le` correspond au corpus d'apprentissage dont les termes sont annotés manuellement
  * l'option `-e` correspond au corpus d'évaluation à désambiguiser
  * l'option `-o` correspond chemin où seront écrit les résultats du corpus d'évaluation désambiguisé

## Le format d'entrée

Les fichiers du corpus doivent être au format tei et conforme à ce standard.
En ce qui concerne la désambiguisation, les fichiers tei doivent contenir des annotations standOff concernant la morphosyntaxe et la terminologie. Les fichiers doivent être conforme au schéma standOff/tei qui se trouve [ici](https://github.com/laurentromary/stdfSpec/blob/AnnArbor/Schemas/standoff-proposal.rnc).   
Voici un exemple de ces annotations et du fulltext tokénisé ci-dessous.

### exemple  d'annotation morphosyntaxique

### exemple d'annotation terminologique

## exemple d'annotations de la désambiguisation

## Liens Utiles

- http://www.atilf.fr/ressources/termith/
- https://github.com/termsuite
- http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/
- https://www.r-project.org/
- http://www.tei-c.org/index.xml

## contact
- simonmeoni@aol.com / simon.meoni@atilf.fr
