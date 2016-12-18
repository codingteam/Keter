param (
    [string] $TargetPath
)

$ErrorActionPreference = 'Stop'

Remove-Item -Recurse "$TargetPath/*"
Copy-Item -Recurse "$PSScriptRoot/../target/site/*" $TargetPath
